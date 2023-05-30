package com.personal.homestayfinder

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.personal.homestayfinder.data.database.AppDatabase
import com.personal.homestayfinder.data.database.daos.CityDao
import com.personal.homestayfinder.data.database.daos.DistrictDao
import com.personal.homestayfinder.data.database.daos.WardDao
import com.personal.homestayfinder.data.database.entities.CityEntity
import com.personal.homestayfinder.data.database.entities.DistrictEntity
import com.personal.homestayfinder.data.database.entities.WardEntity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class HomestayFinderApplication : Application() {
    private lateinit var database: AppDatabase
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if(isFirstRun){
             database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "Address"
            ).build()
            CoroutineScope(Dispatchers.IO).launch{
                database.runInTransaction{
                    val cityDao = database.cityDao()
                    val districtCountyDao = database.districtCountyDao()
                    val wardCommuneDao = database.wardCommuneDao()
                    initDaNangData(cityDao, districtCountyDao, wardCommuneDao)
                    initHaNoiData(cityDao, districtCountyDao, wardCommuneDao)
                    initHCMData(cityDao, districtCountyDao, wardCommuneDao)
                }
            }
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }

    }
    private  fun initHaNoiData(cityDao: CityDao,districtDao: DistrictDao ,wardDao: WardDao){
        val haNoiCity = CityEntity(2, "Hà Nội")

        val haNoiDistricts = listOf(
            "Quận Ba Đình",//8
            "Quận Bắc Từ Liêm",//9
            "Quận Cầu Giấy",//10
            "Quận Đống Đa",//11
            "Quận Hà Đông",//12
            "Quận Hai Bà Trưng",//13
            "Quận Hoàn Kiếm",//14
            "Quận Hoàng Mai",//15
            "Quận Long Biên",//16
            "Quận Nam Từ Liêm",//17
            "Quận Tây Hồ",//18
            "Quận Thanh Xuân",//19
            "Thị xã Sơn Tây",//20
            "Huyện Ba Vì",//21
            "Huyện Chương Mỹ",//22
            "Huyện Đan Phượng",//23
            "Huyện Đông Anh",//24
            "Huyện Gia Lâm",//25
            "Huyện Hoài Đức",//26
            "Huyện Mê Linh",//27
            "Huyện Mỹ Đức",//28
            "Huyện Phú Xuyên",//29
            "Huyện Phúc Thọ",//30
            "Huyện Quốc Oai",//31
            "Huyện Sóc Sơn",//32
            "Huyện Thạch Thất",//33
            "Huyện Thanh Trì",//34
            "Huyện Thường Tín",//35
            "Huyện Ứng Hòa",//36
            "Huyện Thanh Oai"//37
        )
        val hanoiDistrictEntity = haNoiDistricts.mapIndexed{ index , districtName ->
            DistrictEntity(7 + index + 1, 2 , districtName)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cityDao.insertCity(haNoiCity)
            districtDao.insertDistrictCounty(*hanoiDistrictEntity.toTypedArray())

        }
    }
    private  fun initHCMData(cityDao: CityDao,districtDao: DistrictDao ,wardDao: WardDao){
        val hCMCity = CityEntity(3, "Hồ Chí Minh")
        val hCMDistrict = listOf(
            "Quận 1",//38
            "Quận 2",//39
            "Quận 3",//40
            "Quận 4",//41
            "Quận 5",//42
            "Quận 6",//43
            "Quận 7",//44
            "Quận 8",//45
            "Quận 9",//46
            "Quận 10",//47
            "Quận 11",//48
            "Quận 12",//49
            "Quận Tân Bình",//50
            "Quận Bình Thạnh",//51
            "Quận Gò Vấp",//52
            "Quận Phú Nhuận",//53
            "Quận Tân Bình",//54
            "Quận Tân Binh",//55
            "Quận Tân Phú",//56
            "Quận Thủ Đức",//57
            "Huyện Bình Chánh",//58
            "Huyện Cần Giờ",//59
            "Huyện Củ Chi",//60
            "Huyện Hóc Môn",//61
            "Huyện Nhà Bè",//62
        )
        val hCMDistrictEntity = hCMDistrict.mapIndexed{ index , districtName ->
            DistrictEntity(37 + index + 1, 3 , districtName)
        }
        CoroutineScope(Dispatchers.IO).launch {
            cityDao.insertCity(hCMCity)
            districtDao.insertDistrictCounty(*hCMDistrictEntity.toTypedArray())
        }
    }
    private  fun initDaNangData(cityDao: CityDao,districtDao: DistrictDao ,wardDao: WardDao){
        val daNangCity = CityEntity(1,"Đà Nẵng")
        //DistrictCounty data initialization
        val daNangDistricts= listOf(
            "Quận Liên Chiểu",
            "Quận Thanh Khê",
            "Quận Hải Châu",
            "Quận Sơn Trà",
            "Quận Ngũ Hành Sơn",
            "Quận Cẩm Lệ",
            "Xã Hòa Vang"
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
            districtDao.insertDistrictCounty(*daNangDistrictEntities.toTypedArray())
            daNangWardEntities.forEach { wardList ->
                wardDao.insertWard(*wardList.toTypedArray())
            }
        }
    }
}