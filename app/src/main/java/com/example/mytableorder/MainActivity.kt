package com.example.mytableorder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mytableorder.Db.db
import com.example.mytableorder.adapter.MyFragmentStateAdapter
import com.example.mytableorder.databinding.ActivityMainBinding
import com.example.mytableorder.loginSignUp.viewmodel.UserViewModel
import com.example.mytableorder.model.User
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewmodelFactory.AuthViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var imgUri: Uri
    private lateinit var userType: String
//    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private val authViewModelFactory = AuthViewModelFactory(authRepository)
    private val viewModel: UserViewModel by viewModels { authViewModelFactory }
    private val TAG = "userInfo"

   /* private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(binding.toolbar)
        auth = FirebaseAuth.getInstance()


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView

//--------------------------------------------------------------------------
        // 이하에 탭 간 이동 처리 코드
        val tabLayout: TabLayout = findViewById(R.id.tabs)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = MyFragmentStateAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "홈"
                1 -> tab.text = "예약\n내역"
                2 -> tab.text = "가게\n리스트"
                3 -> tab.text = "자유\n게시판"
                4 -> tab.text = "마이\n페이지"
                // 다른 탭에 대한 설정을 추가합니다.
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.homeFragment)
                    1 -> {
                        // 스와이프 동작을 위한 리사이클러뷰가 있는 Fragment로 이동
                        navController.navigate(R.id.infoFragment)
                    }
                    2 -> navController.navigate(R.id.userListFragment)
                    3 -> navController.navigate(R.id.BoardFragment)
                    4 -> navController.navigate(R.id.mypageFragment)
                    // 다른 탭에 대한 액션을 추가합니다.
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 이전에 선택한 탭에 대한 처리 (옵션)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 같은 탭을 다시 선택한 경우에 대한 처리 (옵션)
                when (tab?.position) {
                    0 -> navController.navigate(R.id.homeFragment)
                    1 -> {
                        // 스와이프 동작을 위한 리사이클러뷰가 있는 Fragment로 이동
                        navController.navigate(R.id.infoFragment)
                    }
//                    2 -> navController.navigate(R.id.InfoFragment)
                    3 -> navController.navigate(R.id.BoardFragment)
                    4 -> navController.navigate(R.id.mypageFragment)
                    // 다른 탭에 대한 액션을 추가합니다.
                }
            }
        })


//--------------------------------------------------------------------------

        navView.setupWithNavController(navController)


        //"AppBarConfiguration"은 앱 바의 동작을 설정하는 데 사용됩니다.
        // 첫 번째 매개변수인 setOf(R.id.homeFragment, R.id.InfoFragment)는 상위 레벨 대상의 ID 세트를 나타내며,
        // 이 대상들은 '뒤로' 버튼을 눌렀을 때 앱을 종료하도록 설정됩니다.
        // 두 번째 매개변수인 drawerLayout는 NavigationView가 포함된 DrawerLayout을 지정합니다.
        // 이를 통해 '뒤로' 버튼이나 홈버튼을 눌렀을 때 드로어가 열리도록 설정할 수 있습니다. < GPT설명 >
        // drawer 버튼이 setof()안의 fragment일때 나타남
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.adminHomeFragment,
                R.id.userListFragment,
                R.id.BoardFragment,
                R.id.infoFragment,
                R.id.mypageFragment

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.splashFragment, R.id.loginFragment, R.id.signUpFragment )) {
                supportActionBar?.hide()
                tabLayout.visibility = View.GONE
            }else if(destination.id in listOf(R.id.adminHomeFragment, R.id.adminListFragment, R.id.adminWriteFragment)){
                tabLayout.visibility = View.GONE
            }else {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                tabLayout.visibility = View.VISIBLE
            }


            /*if (destination.id in listOf(
//                    R.id.donateFragment,
//                    R.id.receiveFragment,
//                    R.id.donationsFragment,
                    R.id.foodMapFragment,
//                    R.id.historyFragment,
                    R.id.aboutUsFragment,
                )
            ) {

                supportActionBar?.show()
                supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true) //위로 버튼 활성화
            }*/
        }
        val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById<ImageView>(R.id.imageView)
        viewModel.getUserImage()


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {

                val user = auth.currentUser
                val userEmailText = header.findViewById<TextView>(R.id.useremail)

                user?.let {
                    db.collection("users")
                        .document(it.uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val user = snapshot.toObject(User::class.java)
                            if (user != null) {
                                val userEmail = user.email ?: ""
                                Log.d("$$", "user email : "+ userEmail)
                                userEmailText.text = userEmail
                            }
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Error: ${it.message}")
                        }
                }
            }
        }
        viewModel.getUserImgResponse.observe(this){
            when(it){
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.string, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    imgUri = it.data
                    Glide.with(this)
                        .load(imgUri)
                        .apply(RequestOptions().override(150, 150))
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(imageView)
                }
            }
        }

        viewModel.getUserInfo()
        viewModel.getUserInfoResponse.observe(this){
            if(it is Resource.Success){
                userType = it.data?.get("user_type") as String
            }
        }
        val sharedPref = this.getSharedPreferences("userType", Context.MODE_PRIVATE)
        userType = sharedPref.getString("user_type", "user").toString()


        val navAdminhome = navView.menu.findItem(R.id.adminHome)
        val navRegiRestautrant = navView.menu.findItem(R.id.regiRestaurant)
        navAdminhome.isVisible = userType == "admin"
        navRegiRestautrant.isVisible = userType == "admin"
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                /*R.id.action_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }*/
                R.id.action_home -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                /*R.id.nav_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "TalbeOrder")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(intent, "Share via"))
                    true
                }*/
                R.id.action_feedback -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data =
                        Uri.parse("mailto:" + "goaud2022@gmail.com") // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    if (intent.resolveActivity(this.packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }
                R.id.adminHome -> {
                    if (userType == "admin") {
                        navController.navigate(R.id.adminHomeFragment)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    } else {

                        false
                    }
                }

                R.id.regiRestaurant ->{
                    if (userType == "admin") {
                        navController.navigate(R.id.adminWriteFragment)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    } else {
                        false
                    }
                }

                R.id.myPage->{
                    val myPageTab = tabLayout.getTabAt(4)
                    myPageTab?.select()
                    true
                }

                R.id.logout -> {
                    auth.signOut()
                    val sharedPreference = getSharedPreferences("userType", MODE_PRIVATE)
                    val editor = sharedPreference.edit()

                    editor.remove("user_type")
                    // 전체 삭제는 editor.clear()
                    editor.commit()
                    Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.splashFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    false
                }
            }
        }



    }


    override fun onResume() {
        super.onResume()
        setSupportActionBar(binding.toolbar)
        binding.drawerLayout.closeDrawer(GravityCompat.START)



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




}