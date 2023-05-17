package com.personal.homestayfinder

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import androidx.room.Room
import com.personal.homestayfinder.data.database.AppDatabase
import com.personal.homestayfinder.data.database.entities.CityEntity
import com.personal.homestayfinder.data.database.entities.DistrictEntity
import com.personal.homestayfinder.data.database.entities.WardEntity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class HomestayFinderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "Address"
        ).build()
        CoroutineScope(Dispatchers.IO).launch{
            database.runInTransaction{
                val cityDao = database.cityDao()
                val districtCountyDao = database.districtCountyDao()
                val wardCommuneDao = database.wardCommuneDao()
                //City data initialization
                val daNangCity = CityEntity(1,"Đà Nẵng")
                //DistrictCounty data initialization
                val daNangDistricts= listOf(
                    "Liên Chiểu",
                    "Thanh Khê",
                    "Hải Châu",
                    "Sơn Trà",
                    "Ngũ Hành Sơn",
                    "Cẩm Lệ",
                    "Hòa Vang"
                )
                val daNangDistrictEntities = daNangDistricts.mapIndexed{ index , districtName ->
                    DistrictEntity(index + 1, 1 , districtName)
                }
                //WardCommune data initialization
                //Lien Chieu
                val lienChieuWards = listOf(
                    "Phường Hòa Hiệp Bắc",
                    "Phường Hòa Hiệp Nam",
                    "Phường Hòa Khánh Bắc",
                    "Phường Hòa Khánh Nam",
                    "Phường Hòa Minh"
                )
                //Thanh Khe
                val thanhKheWards = listOf(
                    "Phường Tam Thuận",
                    "Phường Thanh Khê Tây",
                    "Phường Thanh Khê Đông",
                    "Phường Xuân Hà",
                    "Phường Tân Chính",
                    "Phường Chính Gián",
                    "Phường Vĩnh Trung",
                    "Phường Thạc Gián",
                    "Phường An Khê",
                    "Phường Hoa Khê"
                )

                //Hai Chau
                val haiChauWards = listOf(
                    "Phường Thanh Bình",
                    "Phường Thuận Phước",
                    "Phường Thạch Rang",
                    "Phường Hải Châu I",
                    "Phường Hải Châu II",
                    "Phường Phước Ninh",
                    "Phường Hòa Thuận Tây",
                    "Phường Hòa Thuận Đông",
                    "Phường Nam Dương",
                    "Phường Bình Hiên",
                    "Phường Bình Thuận",
                    "Phường Hòa Cường Bắc",
                    "Phường Hòa Cường Nam"
                )
                //Son Tra
                val sonTraWards = listOf(
                    "Phường Thọ Quang",
                    "Phường Nại Hiên Đông",
                    "Phường Mân Thái",
                    "Phường An Hải Bắc",
                    "Phường Phước Mỹ",
                    "Phường An Hải Tây",
                    "Phường An Hải Đông"
                )
                //Ngu Hanh Son
                val nguHanhSonWards = listOf(
                    "Phường Mỹ An",
                    "Phường Khuê Mỹ",
                    "Phường Hòa Quý",
                    "Phường Hòa Hải"
                )
                //Cam Le
                val camLeWards = listOf(
                    "Phường Khuê Trung",
                    "Phường Hòa Phát",
                    "Phường Hòa An",
                    "Phường Hòa Thọ Tây",
                    "Phường Hòa Thọ Đông",
                    "Phường Hòa Xuân"
                )
                //Hoa Vang
                val hoaVangWards = listOf(
                    "Xã Hòa Bắc",
                    "Xã Hòa Liên",
                    "Xã Hòa Ninh",
                    "Xã Hòa Sơn",
                    "Xã Hòa Nhơn",
                    "Xã Hòa Phú",
                    "Xã Hòa Phong",
                    "Xã Hòa Châu",
                    "Xã Hòa Tiến",
                    "Xã Hòa Phước",
                    "Xã Hòa Khương"
                )
                val daNangWardEntities = listOf(
                    lienChieuWards.mapIndexed{ index, wardName ->
                        WardEntity(index +1,1 , wardName)
                    },
                    thanhKheWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size +1, 2, wardName)
                    },
                    haiChauWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size + thanhKheWards.size + 1, 3, wardName)
                    },
                    sonTraWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size + thanhKheWards.size
                                + haiChauWards.size +1, 4, wardName)
                    },
                    nguHanhSonWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size + thanhKheWards.size
                                + haiChauWards.size + sonTraWards.size + 1, 5, wardName)
                    },
                    camLeWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size + thanhKheWards.size
                                + haiChauWards.size + sonTraWards.size + nguHanhSonWards.size + 1, 6, wardName)
                    },
                    hoaVangWards.mapIndexed { index, wardName ->
                        WardEntity(index + lienChieuWards.size + thanhKheWards.size
                                + haiChauWards.size + sonTraWards.size + nguHanhSonWards.size + camLeWards.size +1 , 7, wardName)
                    }
                )
                CoroutineScope(Dispatchers.IO).launch {
                    cityDao.insertCity(daNangCity)
                    districtCountyDao.insertDistrictCounty(*daNangDistrictEntities.toTypedArray())
                    daNangWardEntities.forEach { wardList ->
                        wardCommuneDao.insertWard(*wardList.toTypedArray())
                        val size = wardCommuneDao.getAll().size
                    }
                }
            }
        }
    }
}