package com.example.tableorder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.tableorder.adapter.MyFragmentStateAdapter
import com.example.tableorder.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)



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
                1 -> tab.text = "예약내역"
                2 -> tab.text = "즐겨찾기"
                3 -> tab.text = "자유\n게시판"
                4 -> tab.text = "마이\n페이지"
                // 다른 탭에 대한 설정을 추가합니다.
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> navController.navigate(R.id.homeFragment)
                    1 -> navController.navigate(R.id.aboutUsFragment)
                    2 -> navController.navigate(R.id.homeFragment)
                    3 -> navController.navigate(R.id.InfoActivity)
                    4 -> navController.navigate(R.id.homeFragment)
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
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.splashFragment, R.id.loginFragment)) {
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

        /*val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById<ImageView>(R.id.imageView)
        val userImage = auth.currentUser?.photoUrl
        lifecycleScope.launch {
            whenCreated {
                RepositoryImpl.getInstance().getCurrentUserEmail {
                    val userEmailText = header.findViewById<android.widget.TextView>(R.id.useremail)
                    userEmailText.text = it
                }
            }
        }

        Glide
            .with(this)
            .load(userImage)
            .apply(RequestOptions().override(150, 150))
            .placeholder(R.drawable.ic_person)
            .into(imageView)*/

        /*binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                R.id.action_home -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "FOODONOR")
                    intent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(intent, "Share via"))
                    true
                }
                R.id.action_feedback -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data =
                        Uri.parse("mailto:" + "leencelidoros@gmail.com") // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    if (intent.resolveActivity(this.packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }*/

       /* val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            // 5초 후에 실행할 코드를 여기에 작성
            // 다른 엑티비티로 이동하려면 Intent를 사용하면 됩니다.
            val intent = Intent(this, YourNextActivity::class.java)
            startActivity(intent)
            finish() // 현재 엑티비티를 종료하고 다른 엑티비티로 이동
        }, 5000)*/

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