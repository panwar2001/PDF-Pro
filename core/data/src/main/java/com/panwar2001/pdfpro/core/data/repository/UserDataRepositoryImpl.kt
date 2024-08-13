package com.panwar2001.pdfpro.core.data.repository

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.panwar2001.pdfpro.core.datastore.UserPreferencesDataSource
import com.panwar2001.pdfpro.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource,
    @ApplicationContext private val context: Context
) : UserDataRepository {
    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun setOnboardingDone() {
        preferencesDataSource.onBoardingDone()
    }

    override suspend fun setLightTheme() {
        preferencesDataSource.setTheme(true)
    }

    override suspend fun setDarkTheme() {
        preferencesDataSource.setTheme(false)
    }

    override fun getAppVersion(): String {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown version"
        } catch (e: PackageManager.NameNotFoundException) {
            "Version information not available"
        }
    }
}