package com.example.mytableorder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel() {
    // 수정은 외부에서 직접하지는 못하게, viewModel을 통해서 수정 가능
    private val shopsMap = MutableLiveData<MutableMap<String, RestaurantInfoDTO>>()

    // 수정할 수 없는 값으로 변경해서
    fun getRestaurantList() : LiveData<MutableMap<String, RestaurantInfoDTO>> {
        return shopsMap
    }

    private val curLocation = MutableLiveData<PointD>()

    fun getCurLocation()  : LiveData<PointD> {
        return curLocation
    }

    // 초기화
    init{
        // 여기서 값을 초기화 하면 map이 onready가 되지 않아 Null pointer exception
        shopsMap.value = mutableMapOf<String, RestaurantInfoDTO>()
    }

    fun getRestaurantListFromServer(){
        // observer가 호출되려면 value라는 자체가 변해야 하므로 새로 만들어 대입
        val newList = mutableMapOf<String, RestaurantInfoDTO>()

        var restInfo : RestaurantInfoDTO
        Db.getInstance().collection("shops")
            .get()
            .addOnSuccessListener { restaurantInfos ->
                try{
                    for(rest in restaurantInfos){
                        restInfo = RestaurantInfoDTO(
                            rest.data["name"].toString(),
                            rest.data["latitude"] as Double,
                            rest.data["longitude"] as Double
                        )
                        Log.i("restInfo", "lsit $restInfo")
                        newList[rest.data["name"].toString()] = restInfo
                    }
                } catch(exc : Exception){
                    Log.e("restInfo", "exc ${exc.cause}")
                }

                Log.i("getList", "success : $newList")
                //live 데이터 값은 .value 써야함
                shopsMap.value = newList
            }
            .addOnFailureListener{
                    exc -> Log.i("getList", exc.cause.toString())
            }
    }

    fun setRestaurantList( newMap : MutableMap<String, RestaurantInfoDTO>){
        shopsMap.value= newMap
    }

    fun setCurrentLocation( newLocation : PointD){
        curLocation.value = newLocation
    }
}