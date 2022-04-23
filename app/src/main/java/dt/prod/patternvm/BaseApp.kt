package dt.prod.patternvm

import android.app.Application
import android.content.SharedPreferences
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp
//import dt.prod.patternvm.core.network.TokenRepository


@HiltAndroidApp
class BaseApp : Application() {
    companion object {
        lateinit var instance: BaseApp
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Kotpref.init(this)
        sharedPreferences = getSharedPreferences("default", MODE_PRIVATE)
//        TokenRepository.loadTokenFromShared(
//            sharedPreferences.getString("accessToken", "") ?: ""
//        )
    }
}