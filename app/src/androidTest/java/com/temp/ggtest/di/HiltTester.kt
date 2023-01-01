package com.temp.ggtest.di

import com.temp.ggtest.BaseApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseApplication::class)
interface HiltTester