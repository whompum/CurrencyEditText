package currencyedittext.whompum.com.currencyedittext;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final CurrencyFormatter ourInstance = new CurrencyFormatter();

    //Uses a Locale to append commas and decimals and symbols to the value
    private NumberFormat formatter;

    //Number of decimals the locale formats its currency with. US is 2, JP is 0 for example
    private int numDecimals = 2; //DEF

    private CurrencyFormatter() {
        formatter = NumberFormat.getCurrencyInstance( Locale.getDefault() );
        numDecimals = formatter.getCurrency().getDefaultFractionDigits();
    }

    public static CurrencyFormatter getInstance() {
        return ourInstance;
    }

    /**
     * Main Client API.
     * Simple I/O method. Input a long value and get a cash-formatted string
     * as its output.
     * @param value The # of pennies you want to convert to cash
     * @return The cash converted amount.
     */
    public String convert(final long value){
        return toCash(
                toCurrency( value )
        );
    }


    /**
     * Converts penny values to user locale specific double
     *
     * E.G. in US, 2782 converted would be 27.82 since we use two decimal places for monetary representation
     * @param pennies pennies to convert
     * @return locale formatted double
     */
    public double toCurrency(final long pennies){
        return pennies / ( Math.pow( 10, numDecimals ) );
    }

    /**
     * Converts a decimal formatted penny value into the cash value.
     * E.G. given 27.82 it will output $27.82
     * E.G. given 1234.56 it will output $1,234.56
     *
     * Above examples are assumed for USD currencies.
     *
     * @param cashDouble Cash value to convert
     * @return String formatted value of the double cash.
     */
    public String toCash(final Double cashDouble){
        return formatter.format( cashDouble );
    }

}
