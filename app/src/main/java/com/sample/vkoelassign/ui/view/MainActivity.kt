package com.sample.vkoelassign.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityMainBinding
import com.sample.vkoelassign.network.User
import com.sample.vkoelassign.utility.Pref
import com.sample.vkoelassign.utility.Utils
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var headerViewLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    //handle Navigation and Bottom Navigation item Clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        item.onNavDestinationSelected(navController)
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        menuItemSecected(item)
        return true
    }

    /**
     * Method Handles Bottom Navigation Item Clicks
     */
    private fun menuItemSecected(item: MenuItem) {
        when (item.itemId) {
            //When logOut Drawer item pressed
            R.id.nav_logout -> {
                showConfirmationDialog()
            }

            /* R.id.search_frag -> {
                 //showConfirmationDialog()
             }*/
        }
    }

    /**
     * inital setUps
     */
    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val findNavController: NavController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(findNavController.graph, binding.drawerLayout)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(findNavController, appBarConfiguration)
        binding.toolbar.setupWithNavController(findNavController, appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.img_drawer)
        binding.navigationView.setupWithNavController(findNavController)
        binding.navigationView.setNavigationItemSelectedListener(this)
        // by default first item will be selected
        //binding.navigationView.menu.getItem(0).setChecked(true)
        setUpHeaderView()

        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setTitle("")

        findNavController.addOnDestinationChangedListener { navController, destination, arguments ->
            when (destination.id) {
                R.id.home_frag -> {
                    Utils.showFadeInAnimOnText(
                        this,
                        binding.toolbarTitle,
                        getString(R.string.txt_x_list)
                    )
                    binding.toolbar.title = ""
                }

                R.id.xDetail_frag -> {
                    Utils.showFadeInAnimOnText(
                        this,
                        binding.toolbarTitle,
                        getString(R.string.txt_x_detail)
                    )
                    binding.toolbar.title = ""
                    binding.toolbar.setNavigationIcon(R.drawable.img_arrow_back_white)
                }
            }
        }
    }

    //navigate to back screen
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpHeaderView() {
        headerViewLayout = binding.navigationView.getHeaderView(0)

        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)
                    headerViewLayout.findViewById<TextView>(R.id.nav_username_text_view).text =
                        user?.fullName
                    headerViewLayout.findViewById<TextView>(R.id.nav_user_phone_num_text_view).text =
                        user?.mobileNum
                    Utils.setImage(
                        headerViewLayout.findViewById<CircleImageView>(R.id.profile_image_view),
                        user?.image!!
                    )
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })


    }

    /**
     * Method to Show Language Confirmation Popup
     */
    private fun showConfirmationDialog() {
        val dailog = Utils.makeDialog(R.layout.dialog_confirmation, this@MainActivity)

        val noButton = dailog.findViewById<TextView>(R.id.no_button)
        val yesButton = dailog.findViewById<TextView>(R.id.yes_button)

        noButton.setOnClickListener {
            dailog.dismiss()
        }

        yesButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            dailog.dismiss()
            Pref.clearPref(this)

            val mainIntent = Intent(this@MainActivity, OtpActivity::class.java)
            Utils.launchNewActivity(this@MainActivity, mainIntent, true)
            finish()
        }
    }

}