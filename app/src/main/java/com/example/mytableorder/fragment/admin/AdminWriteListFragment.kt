package com.example.mytableorder.fragment.admin

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.adapter.AdminListAdapter
import com.example.mytableorder.databinding.FragmentAdminWriteListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


// AdminWriteListFragment 클래스는 Fragment를 상속
class AdminWriteListFragment : Fragment() {

    // Firebase Realtime Database와 Firebase Storage에 접근하기 위한 변수를 선언
    private lateinit var db : FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    //이거 안쓰는듯....
    //private var addImageUri: Uri? = null

    // 사용자 입력을 받기 위한 EditText와 Button에 대한 변수를 선언
    private  lateinit var addRaNum: EditText
    private lateinit var addRaName: EditText
    private  lateinit var addBtnImage: Button
    private  lateinit var addRaInfo: EditText
    private  lateinit var addRaMenu: EditText
    private  lateinit var addRaLatitude: EditText
    private  lateinit var addRaLongitude: EditText



    // View Binding을 사용하여 레이아웃과의 상호작용을 쉽게
    private var _binding: FragmentAdminWriteListBinding? = null
    private val binding get() = _binding!!

    // 이미지를 선택하고 결과를 받기 위한 ActivityResultLauncher를 초기화.
    // 사용자가 이미지를 선택하면 이곳에서 처리가 이루어짐.
    private val putImage = registerForActivityResult(ActivityResultContracts.GetContent())
    {uri: Uri? ->
        if (uri != null) {
            // 선택된 이미지를 ImageView에 표시합니다.
            binding.addedImage.visibility = View.VISIBLE
            binding.addedImage.setImageURI(uri)

            // 선택된 이미지를 Firebase Storage에 업로드합니다.
            uploadImageToFirebaseStorage(uri)
        } else {
            Toast.makeText(context,"이미지를 선택하지 않았습니다.",Toast.LENGTH_SHORT).show()
        }



    }

    // Fragment의 뷰를 생성하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Firebase Storage 인스턴스를 초기화합니다.
        storage = FirebaseStorage.getInstance()
        return inflater.inflate(R.layout.fragment_admin_write_list, container, false)
    }

    // 뷰가 생성된 직후에 호출되는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View Binding을 설정합니다.
        _binding = FragmentAdminWriteListBinding.bind(view)

        // 각 입력 필드와 버튼을 레이아웃과 연결합니다.
        addRaNum = binding.raNum
        addRaName = binding.raName
        addRaInfo = binding.raInfo
        addRaMenu = binding.raMenu
        addRaLatitude = binding.raLatitude
        addRaLongitude = binding.raLongitude





        // '이미지 추가' 버튼에 클릭 리스너를 설정합니다.
        binding.btnAddImage.setOnClickListener {
            openImagePicker()
        }

        //val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

        // '등록' 버튼에 클릭 리스너를 설정합니다.
        binding.btnUpdate.setOnClickListener {
            val raNum = addRaNum.text.toString().toInt()
            val raName = addRaName.text.toString()
            val raInfo = addRaInfo.text.toString()
            val raMenu = addRaMenu.text.toString()
            val raLatitude = addRaLatitude.text.toString().toDouble()
            val raLongitude = addRaLongitude.text.toString().toDouble()

            // 사용자가 입력한 데이터를 기반으로 AdminListDTO 객체를 생성합니다.
            val adminListDTO = AdminListDTO(
                raNum = raNum,
                raName = raName,
                raImg = "",
                raInfo = raInfo,
                raMenu = raMenu,
                raLatitude = raLatitude,
                raLongitude = raLongitude
            )

            //중복됐었구나;;;일단 대기
//            // Firebase Realtime Database에 데이터 쓰기
//            val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
//            databaseReference.child(raNum.toString()).setValue(adminListDTO)
//                .addOnSuccessListener {
//                    // 데이터베이스 쓰기 성공 하면 콜백 함수
//                    navigateBackToHome() // 여기서 성공적으로 돌아가는 메소드를 호출.
//                }
//                .addOnFailureListener {exception->
//                    // 데이터베이스 쓰기 실패
//                    Log.e("FirebaseError", "Data write failed", exception)
//                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
//                }
        }
    }

    // 이 메소드는 사용자를 AdminHomeFragment로 돌려보냄
    private fun navigateBackToHome() {
        findNavController().navigateUp() // 또는 findNavController().navigate(R.id.action_specific)
    }
    // 프래그먼트 뷰가 소멸할 때 호출되는 메서드
    // 여기에서 View Binding 인스턴스를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 이미지 선택기 열기 로직 (openImagePicker() 함수 구현 필요)
    private fun openImagePicker() {
        putImage.launch("image/*")
    }

    class AdminListFragment : Fragment() {

        private lateinit var recyclerView: RecyclerView
        private lateinit var listAdapter: AdminListAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // fragment_admin_list.xml에서 리사이클러뷰를 인플레이트
            val view = inflater.inflate(R.layout.fragment_admin_recyclerview_list, container, false)
            recyclerView = view.findViewById(R.id.recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(context)
            listAdapter = AdminListAdapter()
            recyclerView.adapter = listAdapter

            recyclerView.adapter = listAdapter
            fetchRestaurantList() // Firebase Realtime Database에서 데이터 가져오기
            return view
        }


        private fun fetchRestaurantList() {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val restaurantList = mutableListOf<AdminListDTO>()
                    for (restaurantSnapshot in snapshot.children) {
                        val restaurant = restaurantSnapshot.getValue(AdminListDTO::class.java)
                        restaurant?.let { restaurantList.add(it) }
                    }
                    listAdapter.setRestaurants(restaurantList)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        }


    }

    // 이미지를 Firebase Storage에 업로드하고 URL을 가져오는 함수
    /* private fun uploadImageToFirebaseStorage(fileUri: Uri) {
         // 파일명을 현재 시간을 기준으로 설정
         val fileName = "images/${System.currentTimeMillis()}-${fileUri.lastPathSegment}"
         // Firebase Storage에 파일을 업로드하기 위한 참조
         val imageRef = storage.reference.child(fileName)

         // 파일을 업로드하고 성공 시 해당 이미지의 URL을 가져오는 로직
         imageRef.putFile(fileUri)
             .addOnSuccessListener { taskSnapshot ->
                 taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                     val imageUrl = downloadUri.toString()
                     // 이미지 URL을 가져왔으므로 이제 Realtime Database에 저장할 수 있음
                     // 이 예제에서는 이미지 URL을 AdminListDTO 객체에 저장하고 데이터베이스에 업데이트
                     updateRestaurantWithImageUrl(imageUrl)
                 }
             }
             // 업로드 실패 시 토스트 메시지 표시
             .addOnFailureListener {
                 Toast.makeText(context, "이미지 업로드 실패: ${it.message}", Toast.LENGTH_LONG).show()
             }
     }*/
    private fun uploadImageToFirebaseStorage(fileUri: Uri) {
        val fileName = "images/${System.currentTimeMillis()}-${fileUri.lastPathSegment}"
        val imageRef = storage.reference.child(fileName)

        imageRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("download","${taskSnapshot.storage.downloadUrl}")
                taskSnapshot.storage.downloadUrl?.addOnSuccessListener { downloadUri ->

                    val imageUrl = downloadUri.toString()
                    // 업로드된 이미지 URL로 데이터베이스 업데이트
                    updateRestaurantWithImageUrl(imageUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "이미지 업로드 실패: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
    private fun updateRestaurantWithImageUrl(imageUrl: String) {
        val raNum = addRaNum.text.toString().toInt()
        val raName = addRaName.text.toString()
        val raInfo = addRaInfo.text.toString()
        val raMenu = addRaMenu.text.toString()
        val raLatitude = addRaLatitude.text.toString().toDouble()
        val raLongitude = addRaLongitude.text.toString().toDouble()

        // 사용자가 입력한 데이터를 기반으로 AdminListDTO 객체를 생성합니다.
        val adminListDTO = AdminListDTO(
            raNum = raNum,
            raName = raName,
            raImg = imageUrl, // 이미지 URL 저장
            raInfo = raInfo,
            raMenu = raMenu,
            raLatitude = raLatitude,
            raLongitude = raLongitude
        )

        // Firebase Realtime Database에 데이터 쓰기
        val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
        databaseReference.child(raNum.toString()).setValue(adminListDTO)
            .addOnSuccessListener {
                // 데이터베이스 쓰기 성공 시 동작할 로직 추가
                navigateBackToHome()
            }
            .addOnFailureListener { exception ->
                // 데이터베이스 쓰기 실패 시 동작할 로직 추가
                Log.e("FirebaseError", "Data write failed", exception)
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }



}