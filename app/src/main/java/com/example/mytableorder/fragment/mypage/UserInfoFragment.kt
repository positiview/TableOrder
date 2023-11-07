package com.example.mytableorder.fragment.mypage

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentUserInfoBinding
import com.example.mytableorder.loginSignUp.viewmodel.LoginViewModel
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.utils.CheckInternet
import com.example.mytableorder.viewmodelFactory.AuthViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.mytableorder.utils.Resource



class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding

    /*private lateinit var viewModel : MypageViewModel
    private lateinit var viewModelFactory: MypageViewModelFactory*/
    lateinit var nickName: String
    lateinit var filePath: Uri
    private lateinit var imgPath: Uri
    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private val authViewModelFactory = AuthViewModelFactory(authRepository)
    private val viewModel: LoginViewModel by viewModels { authViewModelFactory }


    // 프로필 or 닉네임 변경 여부
    private var imageChanged = false
    private var nickNameChanged = false

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

                if (CheckInternet.isConnected(requireActivity())) {
                    // 모두변경
                    if (nickNameChanged && imageChanged) {
                        /*file = File(createCopyAndReturnRealPath(filePath))
                        requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        bodyFile = MultipartBody.Part.createFormData("image",file.name+"_profile.jpg",requestFile)
                        bodyNickname  = MultipartBody.Part.createFormData("nickName",binding.userNicknameEdit.text.toString())
                        viewmodel?.editUserImage(imagePath)*/
                    }
                    // 사용자 정보 변경
                    else if (nickNameChanged && !imageChanged) {
                        /*if(binding.userNicknameEdit.text.toString().isNotEmpty()){
                            bodyNickname  = MultipartBody.Part.createFormData("nickName",binding.userNicknameEdit.text.toString())
                            viewmodel?.editUserInfo(null,bodyNickname)
                        }
                        else toast("닉네임을 입력해주세요.")*/

                    }
                    // 이미지 변경
                    else if (!nickNameChanged && imageChanged) {
                        /*file = File(filePath)
                        requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        bodyFile = MultipartBody.Part.createFormData("image",file.name+"_profile.jpg",requestFile)*/
                        viewmodel?.editUserImage(filePath)

                    }
                } else{

                    Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }

            }
            // 카메라 버튼 이미지 수정
            accountIvProfileCamera.setOnClickListener {
                showDialog()
            }

            // 수정 버튼 클릭시
            /*btnEditNickname.setOnClickListener {
                binding.userNicknameText.visibility = View.GONE
                binding.userNicknameEdit.visibility = View.VISIBLE
                binding.userNicknameEdit.setText(binding.userNicknameText.text)
            }
            userNicknameEdit.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
                override fun afterTextChanged(s: Editable?) {
                    if (!binding.userNicknameEdit.text.toString().equals(nickName)){
                        binding.btnOk.isEnabled = true
                        nickNameChanged = true
                    }else{
                        binding.btnOk.isEnabled = false
                    }
                }
            })*/
        }
        return view
    }


    private fun initViewModel() {


       /* binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner*/


        // 유저 정보 setting
        viewModel.getUserInfo()
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
                    binding.userEmailText.text = email
                    binding.userNameText.text = username
                    binding.userPhoneNumber.text = phone
                    binding.userLevel.text = level



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
        viewModel.getUserImage()
        viewModel.getUserImgResponse.observe(viewLifecycleOwner){ res ->
            when(res){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    imgPath = res.data
                    if (imgPath == null) {

                    } else {
                        Glide.with(requireContext()).load(imgPath).into(binding.accountIvProfile)
                    }
                }is Resource.Error -> {
                Toast.makeText(requireContext(), "이미지 로딩 에러", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }


    fun showDialog(){
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
//                viewModel.deleteProfileImage()
            }else{
//                    toast("네트워크 연결을 확인해 주세요.")
                builder.dismiss()
            }
        }
        builder.setView(dialogView)
        builder.show()
    }

}