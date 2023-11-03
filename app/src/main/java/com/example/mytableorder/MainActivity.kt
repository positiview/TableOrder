package com.example.mytableorder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mytableorder.R
import com.example.mytableorder.adapter.MyFragmentStateAdapter
import com.example.mytableorder.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
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
                        navController.navigate(R.id.InfoFragment)
                    }
//                    2 -> navController.navigate(R.id.InfoFragment)
//                    3 -> navController.navigate(R.id.InfoFragment)
//                    4 -> navController.navigate(R.id.InfoFragment)
                    // 다른 탭에 대한 액션을 추가합니다.
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 이전에 선택한 탭에 대한 처리 (옵션)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 같은 탭을 다시 선택한 경우에 대한 처리 (옵션)
            }
        })


//--------------------------------------------------------------------------

        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
//                R.id.adminHomeFragment,
//                R.id.donorsHomeFragment
                R.id.InfoFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.splashFragment, R.id.loginFragment, R.id.signUpFragment)) {
                supportActionBar?.hide()
                tabLayout.visibility = View.GONE
            }else {
                supportActionBar?.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                tabLayout.visibility = View.VISIBLE
            }
//            if (destination.id in listOf(
////                    R.id.donateFragment,
////                    R.id.receiveFragment,
////                    R.id.donationsFragment,
//                    R.id.foodMapFragment,
////                    R.id.historyFragment,
//                    R.id.aboutUsFragment,
//                )
//            ) {
//
//                supportActionBar?.show()
//                supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
//            }
        }
        val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById<ImageView>(R.id.imageView)
        /*val userImage = auth.currentUser?.photoUrl
        lifecycleScope.launch {
            whenCreated {
                RepositoryImpl.getInstance().getCurrentUserEmail {
                    val userEmailText = header.findViewById<android.widget.TextView>(R.id.useremail)
                    userEmailText.text = it
                }
            }
        }*/

        /*Glide
            .with(this)
            .load(userImage)
            .apply(RequestOptions().override(150, 150))
            .placeholder(R.drawable.ic_person)
            .into(imageView)*/

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
                R.id.logout -> {
                    auth.signOut()
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                Log.d("status", "Touch Down Event")
            }

            MotionEvent.ACTION_UP->{
                Log.d("status", "Touch Up Event")
            }
        }
        return super.onTouchEvent(event)
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