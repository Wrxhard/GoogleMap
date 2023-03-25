package tdtu.smallproject.midtermdemo

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import tdtu.smallproject.midtermdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val map = supportFragmentManager
            .findFragmentById(R.id.fragment_map) as SupportMapFragment
        binding.place.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextChange(location: String?): Boolean {
                return false
            }
            //khi người dùng nhập vào và submit
            override fun onQueryTextSubmit(location: String?): Boolean {
                var addresses:List<Address>  = mutableListOf()
                if (location!="") {
                    val geocoder = Geocoder(this@MainActivity)  //class để danh sách các kết quả tìm kiếm dựa vào tên vị trí bao gồm cả kinh độ và vĩ độ
                    try {
                        if (location != null) {
                            if (Build.VERSION.SDK_INT >= 33) {
                                // Khi Android SDK >= 33 thì sử dụng câu lệnh này để lấy một danh sách kết quả tìm kiếm mà người dùng nhập vào
                                geocoder.getFromLocationName(location, 1) {
                                    addresses = it
                                }
                            } else {
                                addresses = geocoder.getFromLocationName(location,1) as List<Address>
                                // Khi Android SDK < 33 thì sử dụng câu lệnh này để lấy một danh sách kết quả tìm kiếm mà người dùng nhập vào
                            }
                        }
                    } catch (e: Exception) {
                        Log.i("exception", "${e}")
                    }
                    val address = addresses[0] //lấy phần tử đầu tiên của kết quả trả về
                    val latitude = address.latitude //vĩ độ của vị trí
                    val longitude = address.longitude //kinh độ của vị trí
                    val latLng = LatLng(latitude, longitude) //sau khi có được kinh độ và vĩ độ ta đưa nó vào một class dùng đại diện cho vị trí
                    googleMap.clear()  //xóa mọi marker,polyline,... đã set trước đó
                    googleMap.resetMinMaxZoomPreference()  //reset zoom setting
                    googleMap.addMarker(MarkerOptions().position(latLng).title("$location")) //set marker đựa vào vị trí người dùng nhập vào
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))  //chuyển hướng camera về vị trí người dùng nhập vào
                    googleMap.setMinZoomPreference(6.0F)   //set min zoom
                }
                return false
            }
        })
        map.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap=map
    }
}