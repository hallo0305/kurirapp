package com.example.kurirapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kurirapp.adapter.UserAdapter
import com.example.kurirapp.model.UserResponse
import com.example.kurirapp.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSearchName: EditText
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewErrorMessage: TextView
    private lateinit var textViewEmptyState: TextView

    private lateinit var userAdapter: UserAdapter
    private var daftarPenggunaAsli: List<UserResponse> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupSearchListener()
        fetchUsers()
    }

    private fun initViews() {
        editTextSearchName = findViewById(R.id.editTextSearchName)
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        progressBar = findViewById(R.id.progressBar)
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage)
        textViewEmptyState = findViewById(R.id.textViewEmptyState)
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(listOf())
        recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }

    private fun setupSearchListener() {
        editTextSearchName.doAfterTextChanged { editable ->
            val query = editable.toString().trim()
            filterUsers(query)
        }
    }

    private fun filterUsers(query: String) {
        val filteredList = if (query.isEmpty()) {
            daftarPenggunaAsli
        } else {
            daftarPenggunaAsli.filter { user ->
                user.name.contains(query, ignoreCase = true) ||
                        user.company.companyName.contains(query, ignoreCase = true)
            }
        }

        userAdapter.perbaruiDaftar(filteredList)

        if (filteredList.isEmpty() && daftarPenggunaAsli.isNotEmpty()) {
            recyclerViewUsers.visibility = View.GONE
            textViewEmptyState.visibility = View.VISIBLE
        } else {
            recyclerViewUsers.visibility = View.VISIBLE
            textViewEmptyState.visibility = View.GONE
        }
    }

    private fun fetchUsers() {
        progressBar.visibility = View.VISIBLE
        recyclerViewUsers.visibility = View.GONE
        textViewErrorMessage.visibility = View.GONE
        textViewEmptyState.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getAllUsers()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        daftarPenggunaAsli = response.body()!!
                        userAdapter.perbaruiDaftar(daftarPenggunaAsli)
                        recyclerViewUsers.visibility = View.VISIBLE
                    } else {
                        showError("Gagal memuat data kurir (HTTP ${response.code()})")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    showError("Terjadi kesalahan jaringan: ${e.localizedMessage ?: "Periksa koneksi Anda"}")
                }
            }
        }
    }

    private fun showError(message: String) {
        textViewErrorMessage.text = message
        textViewErrorMessage.visibility = View.VISIBLE
        recyclerViewUsers.visibility = View.GONE
    }
}
