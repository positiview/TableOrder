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
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentAdminWriteListBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AdminWriteListFragment : Fragment() {

    //파피어베이스 연결,스토리지 연결,이미지 uri 추가
    private lateinit var db : FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var addImageUri: Uri? = null

    //등록 해야될 입력 값들 넣기
    private  lateinit var addRaNum: EditText
    private lateinit var addRaName: EditText
    private  lateinit var addBtnImage: Button
    private  lateinit var addRaInfo: EditText
    private  lateinit var addRaMenu: EditText
    private  lateinit var addRaLatitude: EditText
    private  lateinit var addRaLongitude: EditText



    // View Binding을 사용하여 인스턴스 생성
    private var _binding: FragmentAdminWriteListBinding? = null
    private val binding get() = _binding!!

    //이미지 넣기
    private val putImage = registerForActivityResult(ActivityResultContracts.GetContent())
    {uri: Uri? ->
        if (uri != null) {
            //ImageView에 이미지 표시
            binding.addedImage.visibility = View.VISIBLE
            binding.addedImage.setImageURI(uri)

            //Firebase Storage에 이미지 업로드
            uploadImageToFirebaseStorage(uri)
        } else {
            Toast.makeText(context,"이미지를 선택하지 않았습니다.",Toast.LENGTH_SHORT).show()
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //storage초기화
        storage = FirebaseStorage.getInstance()
        return inflater.inflate(R.layout.fragment_admin_write_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //바인딩설정
        _binding = FragmentAdminWriteListBinding.bind(view)


        addRaNum = binding.raNum
        addRaName = binding.raName
        addRaInfo = binding.raInfo
        addRaMenu = binding.raMenu
        addRaLatitude = binding.raLatitude
        addRaLongitude = binding.raLongitude

        binding.btnAddImage.setOnClickListener {
            openImagePicker()
        }

        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

            binding.btnUpdate.setOnClickListener {
            val raNum = addRaNum.text.toString().toInt()
            val raName = addRaName.text.toString()
            val raInfo = addRaInfo.text.toString()
            val raMenu = addRaMenu.text.toString()
            val raLatitude = addRaLatitude.text.toString().toDouble()
            val raLongitude = addRaLongitude.text.toString().toDouble()

            val adminListDTO = AdminListDTO(
                raNum = raNum,
                raName = raName,
                raImg = "",
                raInfo = raInfo,
                raMenu = raMenu,
                raLatitude = raLatitude,
                raLongitude = raLongitude
            )

            // Firebase Realtime Database에 데이터 쓰기
            val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
            databaseReference.child(raNum.toString()).setValue(adminListDTO)
                .addOnSuccessListener {
                    // 데이터베이스 쓰기 성공
                    navigateBackToHome() // 여기서 성공적으로 돌아가는 메소드를 호출합니다.
                }
                .addOnFailureListener {exception->
                    // 데이터베이스 쓰기 실패
                    Log.e("FirebaseError", "Data write failed", exception)
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun navigateBackToHome() {
        // 이 메소드는 사용자를 AdminHomeFragment로 돌려보냅니다.
        findNavController().navigateUp() // 또는 findNavController().navigate(R.id.action_specific)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 이미지 선택기 열기 로직 (openImagePicker() 함수 구현 필요)
    private fun openImagePicker() {
        putImage.launch("image/*")
    }
    // 이미지를 Firebase Storage에 업로드하고 URL을 가져오는 함수
    // 이미지를 Firebase Storage에 업로드하고 URL을 가져오는 함수
    private fun uploadImageToFirebaseStorage(fileUri: Uri) {
        // Storage 참조를 가져오고 이미지 파일의 이름을 설정합니다.
        val fileName = "images/${System.currentTimeMillis()}-${fileUri.lastPathSegment}"
        val imageRef = storage.reference.child(fileName)

        imageRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    // 이미지 URL을 가져왔으므로 이제 Realtime Database에 저장할 수 있습니다.
                    // 이 예제에서는 이미지 URL을 AdminListDTO 객체에 저장하고 있습니다.
                    // adminListDTO.raImg = imageUrl
                    // updateRestaurantWithImageUrl(imageUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "이미지 업로드 실패: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

}