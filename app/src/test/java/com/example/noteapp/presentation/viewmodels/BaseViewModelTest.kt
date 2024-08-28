package com.example.noteapp.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
}
