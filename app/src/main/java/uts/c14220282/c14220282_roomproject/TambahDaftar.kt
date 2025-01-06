package uts.c14220282.c14220282_roomproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import uts.c14220282.c14220282_roomproject.database.daftarBelanja
import uts.c14220282.c14220282_roomproject.database.daftarBelanjaDB
import uts.c14220282.c14220282_roomproject.helper.DateHelper.getCurrentDate

class TambahDaftar : AppCompatActivity() {
//    coba
    var DB = daftarBelanjaDB.getDatabase(this)
    var tanggal = getCurrentDate()
    var iID : Int = 0
    var iAddEdit : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tambah_daftar_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var _btnTambah = findViewById<Button>(R.id.btnTambah)
        var _etItem = findViewById<EditText>(R.id.etItem)
        var _etJumlah = findViewById<EditText>(R.id.etJumlah)
        var _btnUpdate = findViewById<Button>(R.id.btnUpdate)


        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftarBelanjaDAO().insert(
                    daftarBelanja(
                        tanggal = tanggal,
                        item = _etItem.text.toString(),
                        jumlah = _etJumlah.text.toString()
                    )
                )
            }
        }

        _btnUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftarBelanjaDAO().update(
                    isi_tanggal = tanggal,
                    isi_item = _etItem.text.toString(),
                    isi_jumlah = _etJumlah.text.toString(),
                    isi_status = 0,
                    pilihid = iID
                )
            }
        }


        iID = intent.getIntExtra("id", 0)
        iAddEdit = intent.getIntExtra("addEdit",0)

        if(iAddEdit == 0) {
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etItem.isEnabled = true
        } else {
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etItem.isEnabled = false

            CoroutineScope(Dispatchers.IO).async {
                val item = DB.fundaftarBelanjaDAO().getItem(iID)
                _etItem.setText(item.item)
                _etJumlah.setText(item.jumlah)
            }
        }

    }
}