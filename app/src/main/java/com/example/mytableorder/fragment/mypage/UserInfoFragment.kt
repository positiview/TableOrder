package com.example.mytableorder.fragment.mypage

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentUserInfoBinding
import com.example.mytableorder.viewModel.UserViewModel
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.utils.CheckInternet
import com.example.mytableorder.viewmodelFactory.AuthViewModelFactory
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.mytableorder.model.UpdateUser
import com.example.mytableorder.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding

    /*private lateinit var viewModel : MypageViewModel
    private lateinit var viewModelFactory: MypageViewModelFactory*/
    lateinit var nickName: String
    lateinit var filePath: Uri

    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private val authViewModelFactory = AuthViewModelFactory(authRepository)
    private val viewModel: UserViewModel by activityViewModels() { authViewModelFactory }



    // 프로필 or 닉네임 변경 여부
    private var imageChanged = false
    private var nickNameChanged = false
    private var userCancelled = false
    private var removeImgOrder = false

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            Toast.makeText(requireContext(), "선택한 이미지로 변경했습니다.", Toast.LENGTH_SHORT).show()
            Glide.with(requireContext()).load(result.uriContent).into(binding.accountIvProfile)
            filePath = result.uriContent!!
            binding.btnOk.isEnabled = true
            binding.btnOk.setTextColor(Color.WHITE)
            imageChanged = true
        }
    }

    private fun startCrop() {
        // start picker to get image for cropping and then use the image in cropping activity
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setAllowRotation(true)
                setActivityTitle("crop")
                setCropMenuCropButtonIcon(0)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)


        initViewModel()

        val view = binding.root
        if (CheckInternet.isConnected(requireActivity())) {

            binding.contentContraintlayout.visibility = View.VISIBLE
            binding.disconnectedLayout.visibility = View.GONE
            binding.btnOk.visibility = View.VISIBLE
            /*if(!imageChanged)
                viewModel.getUserInfo()*/
        } else{

            binding.contentContraintlayout.visibility = View.GONE
            binding.disconnectedLayout.visibility = View.VISIBLE
            binding.btnOk.visibility = View.GONE

        }



        // 완료 버튼 눌렀을때 처리
        binding.apply {
            btnOk.setOnClickListener {
                Log.d("$$","btnOk 누름")
                if (CheckInternet.isConnected(requireActivity())) {
                    // 모두변경
                    if (nickNameChanged && imageChanged) {
                        if(binding.userEmailEditText.text.toString().isNotEmpty()){

                            val name= binding.userNameEditText.text.toString()
                            val phone =  binding.userPhoneNumberEdit.text.toString()
                            val user = UpdateUser(name,phone)

                            if (!userCancelled) {
                                viewModel?.editUserInfo(user)
                            }
                        }
                        else {Toast.makeText(requireContext(), "이메일은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show()}

                        viewModel?.editUserImage(filePath)

                    }
                    // 사용자 정보만 변경
                    else if (nickNameChanged && !imageChanged) {

                        if(binding.userEmailEditText.text.toString().isNotEmpty()){
                            val name= binding.userNameEditText.text.toString()
                            val phone =  binding.userPhoneNumberEdit.text.toString()
                            val user = UpdateUser(name,phone)

                            if (!userCancelled) {
                                viewModel?.editUserInfo(user)
                            }


                        }
                        else Toast.makeText(requireContext(), "이메일은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show()

                    }
                    // 이미지만 변경
                    else if (!nickNameChanged && imageChanged) {

                        viewModel?.editUserImage(filePath)

                    }
                } else{

                    Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                // 유저 이미지 삭제
                if(removeImgOrder){
                    deleteUserImage()
                }
                // 버튼 누른후에...
                Toast.makeText(requireContext(), "회원 정보 수정 완료!!", Toast.LENGTH_SHORT).show()
                btnEdit.isChecked = false
                binding.btnOk.isEnabled = false
                imageChanged = false
                nickNameChanged = false
                userCancelled = false
                removeImgOrder = false
            }
            // 카메라 버튼 이미지 수정
            accountIvProfileCamera.setOnClickListener {
                cameraDialog()
            }

            // 수정 버튼 클릭시
            btnEdit.setOnCheckedChangeListener{ _, isChecked ->

                if(isChecked){

                    binding.userEmailTextView.visibility = View.GONE
                    binding.userNameTextView.visibility = View.GONE
                    binding.userPhoneNumberView.visibility = View.GONE
                    binding.userEmailEditText.visibility = View.VISIBLE
                    binding.userNameEditText.visibility = View.VISIBLE
                    binding.userPhoneNumberEdit.visibility = View.VISIBLE
                    btnOk.setTextColor(Color.WHITE)
                    binding.btnOk.isEnabled = true
                    nickNameChanged = true
                }else{
                    btnEdit.text = getString(R.string.edit)
                    binding.userEmailTextView.visibility = View.VISIBLE
                    binding.userNameTextView.visibility = View.VISIBLE
                    binding.userPhoneNumberView.visibility = View.VISIBLE
                    binding.userEmailEditText.visibility = View.GONE
                    binding.userNameEditText.visibility = View.GONE
                    binding.userPhoneNumberEdit.visibility = View.GONE
                    btnOk.setTextColor(Color.parseColor("#FF9674"))
                    binding.btnOk.isEnabled = false
                    nickNameChanged = false
                }
            }

        }
        return view
    }

    private fun editAuthEmail(email: String) {
        val user = Firebase.auth.currentUser
        user!!.updateEmail(binding.userEmailEditText.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("$$", "UserDTO email address updated.")
                    Toast.makeText(requireContext(), "사용자 이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun initViewModel() {


        // 유저 정보 setting
//        viewModel.getUserInfo()
        viewModel.getUserInfoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.infoLinearLayout.visibility = View.GONE
                    binding.imageConstraintLayout.visibility = View.GONE
                    binding.progressbar2.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.infoLinearLayout.visibility = View.VISIBLE
                    binding.imageConstraintLayout.visibility = View.VISIBLE
                    binding.progressbar2.visibility = View.GONE
                    val email = it.data?.get("email") as String?
                    val username = it.data?.get("name") as String?
                    val phone = it.data?.get("phone") as String?
                    val level = it.data?.get("level") as String?
                    binding.userEmailTextView.text = email
                    binding.userNameTextView.text = username
                    binding.userPhoneNumberView.text = phone
                    binding.userLevel.text = level
                    binding.userEmailEditText.setText(email)
                    binding.userNameEditText.setText(username)
                    binding.userPhoneNumberEdit.setText(phone)


                }

                else -> {
                    binding.infoLinearLayout.visibility = View.VISIBLE
                    binding.imageConstraintLayout.visibility = View.VISIBLE
                    binding.progressbar2.visibility = View.GONE
                    Toast.makeText(requireContext(), "유저정보 로딩 에러", Toast.LENGTH_SHORT).show()
                }
            }

            // 프로필이미지 or 닉네임 변경
            /* viewModel.getPutProfileResponse.observe(this){
                if (it.success){
                    toast("성공적으로 수정하였습니다.")
                    *//*if(it.response.newNickName!=null)
                        MyApplication.prefs.setUserNickname(it.response.newNickName)*//*
                    finish()
                }
                else{ toast(it.error.message) }
            }*/

            // 프로필 이미지 삭제
            /*viewModel.getDeleteProfileImage.observe(this){
                if (it.success){
                    toast(it.response)
                    binding.btnOk.isEnabled = false
                    imageChanged = false
                }
            }*/
        }

        viewModel.getUserImgResponse.observe(viewLifecycleOwner){ res ->
            when(res){
                is Resource.Loading -> {
                    binding.infoLinearLayout.visibility = View.GONE
                    binding.imageConstraintLayout.visibility = View.GONE
                    binding.progressbar2.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.infoLinearLayout.visibility = View.VISIBLE
                    binding.imageConstraintLayout.visibility = View.VISIBLE
                    binding.progressbar2.visibility = View.GONE
                    val imgUri: Uri? = res.data

                    Glide.with(requireContext()).load(imgUri).error(R.drawable.img_user).into(binding.accountIvProfile)

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), res.string, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun deleteUserImage(){
        viewModel.deleteUserImage()
        viewModel.getUserImgResponse.observe(viewLifecycleOwner){
            if(it is Resource.Success){
                Toast.makeText(requireContext(), "이미지 삭제 완료", Toast.LENGTH_SHORT).show()
            }else if(it is Resource.Error){
                Toast.makeText(requireContext(), it.string ,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cameraDialog(){
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.profile_edit_dialog,null)
        val changeProfile = dialogView.findViewById<TextView>(R.id.change_profile)
        val removeProfile = dialogView.findViewById<TextView>(R.id.remove_profile)

        changeProfile.setOnClickListener {
            startCrop()
            builder.dismiss()
        }
        removeProfile.setOnClickListener {
            Glide.with(requireContext()).load(R.drawable.img_user).into(binding.accountIvProfile)
            if (CheckInternet.isConnected(requireActivity())) {
                removeImgOrder = true
                binding.btnOk.isEnabled = true
                binding.btnOk.setTextColor(Color.WHITE)
                imageChanged = false
                builder.dismiss()
            }else{
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요" ,Toast.LENGTH_SHORT).show()
                builder.dismiss()
            }
        }
        builder.setView(dialogView)
        builder.show()
    }

    /* private fun showDialog2(email: String){
         val builder = AlertDialog.Builder(requireContext()).create()
         val dialogView = layoutInflater.inflate(R.layout.confirm_dialog,null)
         val confirmBtn = dialogView.findViewById<MaterialButton>(R.id.dialogConfirm)
         val cancelBtn = dialogView.findViewById<MaterialButton>(R.id.dialogCancel)

         confirmBtn.setOnClickListener{
             userCancelled = false
             editAuthEmail(email)
             builder.dismiss()
         }
         cancelBtn.setOnClickListener {
             userCancelled = true
             builder.dismiss()
         }
         builder.setView(dialogView)
         builder.show()
     }*/
}