package com.example.calculator

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.example.calculator.business.dependencies.*
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric

open class CalculatorApplication: Application(){

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        createApplicationComponent()

        Fabric.with(this, Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build())

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    protected open fun createApplicationComponentBuilder(): DaggerApplicationComponent.Builder {
        return DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .managerModule(ManagerModule())
            .repositoryModule(RepositoryModule())
    }

    private fun createApplicationComponent() {
        applicationComponent = createApplicationComponentBuilder().build()
    }


}