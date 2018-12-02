package currencyedittext.whompum.com.currencyedittext;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

public class CurrencyFormatterTest {

    private CurrencyFormatter sut = CurrencyFormatter.getInstance();

    @Test
    public void longToCurrencyDouble_isAcurate_True(){

        final long testAmount = 2782L;

        final double properDoubleAmount = 27.82D; //For USD conversions

        assertThat( sut.toCurrency( testAmount ), is( properDoubleAmount ) );
    }

    @Test
    public void longToCash_isAcurate_True(){

        final long testAmount = 2782L;

        final String properAmount = "$27.82"; //For USD conversions

        assertEquals( sut.convert( testAmount ), properAmount );
    }

    @Test
    public void bigLongToDouble_isAcurate_True(){

        final long testAmount = Long.MAX_VALUE;

        final double properDoubleAmount = 9_223_372_036_854_775_8.07D;

        assertThat( sut.toCurrency( testAmount ), is( properDoubleAmount ));
    }

    @Test
    public void negativeValue_formatToDouble_isAccurate(){

        final long testAmount = -2782L;

        final double properDoubleAmount = -27.82D;

        assertThat( sut.toCurrency( testAmount ), is( properDoubleAmount ));
    }

}