package ru.geekbrains.calculator.ui

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
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
    fun restore_state_presenter_with_dot() {
        val ENTERED_VALUE = "12.9"
        presenter.setNumber(ENTERED_VALUE)
        verify(view, times(1)).setViewNumber(ENTERED_VALUE)
    }

    @Test
    fun restore_incorrect_state_presenter() {
        Assert.assertThrows(NumberFormatException::class.java) { presenter.setNumber("Free text instead of a number") }
    }

    @Test
    fun in_order_entering_all_numbers_multiply_in_the_end() {

        val inOrder = inOrder(view, calculator)

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

        `when`(calculator.setOperator(any(), any())).thenReturn(BigDecimal.valueOf(1234567890))

        presenter.keyMulPressed()

        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("1")
        inOrder.verify(view, times(1)).setViewNumber("12")
        inOrder.verify(view, times(1)).setViewNumber("123")
        inOrder.verify(view, times(1)).setViewNumber("1234")
        inOrder.verify(view, times(1)).setViewNumber("12345")
        inOrder.verify(view, times(1)).setViewNumber("123456")
        inOrder.verify(view, times(1)).setViewNumber("1234567")
        inOrder.verify(view, times(1)).setViewNumber("12345678")
        inOrder.verify(view, times(1)).setViewNumber("123456789")
        inOrder.verify(view, times(1)).setViewNumber("1234567890")
        inOrder.verify(calculator, times(1)).setOperator(
            Calculator.Operations.MUL, BigDecimal.valueOf(1234567890)
        )
        inOrder.verify(view, times(1)).setViewNumber("1234567890")
    }

    @Test
    fun in_order_calculate_div_on_zero() {

        val inOrder = inOrder(view, calculator)

        presenter.keyEightPressed()
        `when`(calculator.setOperator(any(), any())).thenReturn(BigDecimal("8"))
        presenter.keyDivPressed()
        presenter.keyZeroPressed()
        `when`(calculator.result(any())).thenReturn(null)
        presenter.keyResultPressed()

        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("8")
        inOrder.verify(calculator, times(1))
            .setOperator(Calculator.Operations.DIV, BigDecimal.valueOf(8))
        inOrder.verify(view, times(1)).setViewNumber("8")
        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(calculator, times(1)).result(BigDecimal.valueOf(0))
        inOrder.verify(view, times(1)).setViewNumber("error division by zero")
    }

    @Test
    fun in_order_entering_number_with_dot() {

        val inOrder = inOrder(view)

        presenter.keyDotPressed()
        presenter.keyZeroPressed()
        presenter.keySevenPressed()
        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("0.")
        inOrder.verify(view, times(1)).setViewNumber("0.0")
        inOrder.verify(view, times(1)).setViewNumber("0.07")
    }

    @Test
    fun in_order_try_enter_number_with_double_dot() {

        val inOrder = inOrder(view)

        presenter.keyThreePressed()
        presenter.keyDotPressed()
        presenter.keyZeroPressed()
        presenter.keySevenPressed()
        presenter.keyDotPressed()
        presenter.keyEightPressed()

        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("3")
        inOrder.verify(view, times(1)).setViewNumber("3.")
        inOrder.verify(view, times(1)).setViewNumber("3.0")
        inOrder.verify(view, times(1)).setViewNumber("3.07")
        inOrder.verify(view, times(1)).setViewNumber("3.078")
    }

    @Test
    fun in_order_enter_dot_and_delete_dot() {

        val inOrder = inOrder(view)

        presenter.keyDotPressed()
        presenter.keyDelPressed()
        presenter.keyFourPressed()

        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("0.")
        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("4")
    }

    @Test
    fun in_order_enter_dot_after_first_number_and_delete_dot() {

        val inOrder = inOrder(view)

        presenter.keyFivePressed()
        presenter.keyDotPressed()
        presenter.keyDelPressed()
        presenter.keyNinePressed()

        inOrder.verify(view, times(1)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("5")
        inOrder.verify(view, times(1)).setViewNumber("5.")
        inOrder.verify(view, times(1)).setViewNumber("5")
        inOrder.verify(view, times(1)).setViewNumber("59")
    }

    @Test
    fun in_order_enter_dot_in_the_end() {

        val inOrder = inOrder(view)

        presenter.keyZeroPressed()
        presenter.keyNinePressed()
        presenter.keyDotPressed()

        inOrder.verify(view, times(2)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("9")
        inOrder.verify(view, times(1)).setViewNumber("9.")
    }

    @Test
    fun in_order_enter_zero_before_dot_and_after() {

        val inOrder = inOrder(view)

        presenter.keyZeroPressed()
        presenter.keyZeroPressed()
        presenter.keyZeroPressed()
        presenter.keyDotPressed()
        presenter.keyZeroPressed()
        presenter.keyZeroPressed()
        presenter.keyZeroPressed()

        inOrder.verify(view, times(4)).setViewNumber("0")
        inOrder.verify(view, times(1)).setViewNumber("0.")
        inOrder.verify(view, times(1)).setViewNumber("0.0")
        inOrder.verify(view, times(1)).setViewNumber("0.00")
        inOrder.verify(view, times(1)).setViewNumber("0.000")
    }
}