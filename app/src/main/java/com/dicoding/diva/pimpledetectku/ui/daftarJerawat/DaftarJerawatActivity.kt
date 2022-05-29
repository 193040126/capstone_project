package com.dicoding.diva.pimpledetectku.ui.daftarJerawat

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.diva.pimpledetectku.R
import com.dicoding.diva.pimpledetectku.ViewModelFactory
import com.dicoding.diva.pimpledetectku.adapter.ListAcneAdapter
import com.dicoding.diva.pimpledetectku.api.AcneItems
import com.dicoding.diva.pimpledetectku.databinding.ActivityDaftarJerawatBinding
import com.dicoding.diva.pimpledetectku.model.UserPreference
import com.dicoding.diva.pimpledetectku.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DaftarJerawatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarJerawatBinding
    private lateinit var daftarJerawatViewModel: DaftarJerawatViewModel
    private lateinit var token: String

    companion object {
        const val EXTRA_TOKEN = "extra_token"
        const val TAG = "DaftarJerawatActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarJerawatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.getStringExtra(EXTRA_TOKEN).toString()
        val bundle = Bundle()
        bundle.putString(EXTRA_TOKEN, token)

        setupView()

        setupViewModel()

        daftarJerawatViewModel.listAcnes.observe(this, {
            setupAction(it)
        })

        daftarJerawatViewModel.getUser().observe(this,{user->
            Log.d(TAG,"isLogin: ${user.isLogin}")
            if(user.isLogin){
                user.token.let { daftarJerawatViewModel.getAcnesList(user.token) }
            }
        })

        daftarJerawatViewModel.isLoading.observe(this,{
            showLoading(true)
        })

    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(listAcnes: ArrayList<AcneItems>) {
        val adapter = ListAcneAdapter(listAcnes)
        binding.apply {
            acnesRv.layoutManager = LinearLayoutManager(this@DaftarJerawatActivity)
            acnesRv.setHasFixedSize(true)
            acnesRv.adapter = adapter
        }
    }

    private fun setupViewModel() {
        daftarJerawatViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DaftarJerawatViewModel::class.java]

        daftarJerawatViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                val actionBar = supportActionBar
                actionBar!!.title = getString(R.string.menu_daftar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}