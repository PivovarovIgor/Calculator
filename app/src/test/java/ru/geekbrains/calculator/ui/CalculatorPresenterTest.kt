package ru.geekbrains.calculator.ui

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.MockitoAnnotations.initMocks
import ru.geekbrains.calculator.domain.Calculator
import java.math.BigDecimal

class CalculatorPresenterTest {

    private lateinit var presenter: CalculatorPresenter

    @Mock
    private lateinit var calculator: Calculator

    @Mock
    private lateinit var view: CalculatorPresenter.CalculatorView

    @Before
    fun setUp() {
        initMocks(this)
        presenter = CalculatorPresenter(view, calculator)
    }

    @Test
    fun press_one_button() {
        presenter.keyOnePressed()
        verify(view, times(1)).setViewNumber("1")
    }

    @Test
    fun set_view_on_stated_presenter() {
        presenter.keySevenPressed()
        presenter.setView(view)
        verify(view, times(2)).setViewNumber("7")
    }

    @Test
    fun restore_state_presenter() {
        val ENTERED_VALUE = "1234567890"
        presenter.setNumber(ENTERED_VALUE)
        verify(view, times(1)).setViewNumber(ENTERED_VALUE)
    }

    @Test
    fun restore_incorrect_state_presenter() {
        Assert.assertThrows(NumberFormatException::class.java) { presenter.setNumber("Free text instead of a number") }
    }

    @Test
    fun calculate_multiple() {

        val ENTERED_VALUE = "1234567890"

        presenter.keyOnePressed()
        presenter.keyTwoPressed()
        presenter.keyThreePressed()
        presenter.keyFourPressed()
        presenter.keyFivePressed()
        presenter.keySixPressed()
        presenter.keySevenPressed()
        presenter.keyEightPressed()
        presenter.keyNinePressed()
        presenter.keyZeroPressed()

        `when`(calculator.setOperator(any(), any())).thenReturn(BigDecimal(ENTERED_VALUE))

        presenter.keyMulPressed()

        verify(view, atLeastOnce()).setViewNumber(ENTERED_VALUE)
        verify(calculator, times(1)).setOperator(
            Calculator.Operations.MUL,
            BigDecimal(ENTERED_VALUE)
        )
    }
}