package currencyedittext.whompum.com.currencyedittext;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by bryan on 12/13/2017.
 *
 * Converts long values into cash values, then formats according to the users locale.
 * Very simply class just enter your value in pennies, and this class will do the rest
 *
 *
 * The way this class works is by stripping all non-numeric characters from its input
 * Then converting the raw penny value to a decimal by dividing by the locales
 * number of decimals; Lastly, it hands the double off to the formatter who will spit
 * out a locale-formatted version. E.G. in US dollars:
 *
 * 2d7ff8#df2%^^s will be cleaned to spit out
 * '2782'
 *  Then from there we will convert to a double following the number of decimals at the current locale
 *  2782 / Math.pow(10, numFractionDigits) -> 27.82
 *  Lastly, we'll format to the users local.
 *  formatterReference.format(doubleValue)
 */

public class CurrencyEditText extends AppCompatEditText {

    private static final String TAG = "CurrerncyEditText";



    private long pennies = 0;

    private NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

    //Number of decimals the locale formats its currency with. US is 2, JP is 0 for example
    private int numDecimals = formatter.getCurrency().getDefaultFractionDigits();


    private OnCurrencyChange listener = null;


    public CurrencyEditText(Context context) {
        super(context);
        addTextChangedListener(getTextWatcher());
        setText("0");
    }

    public CurrencyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(getTextWatcher());
        setText("0");
        setMovementMethod(null);
    }

    public CurrencyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(getTextWatcher());
        setText("0");
        setMovementMethod(null);
    }



    public TextWatcher getTextWatcher(){
        return textWatcher;
    }

    /**
     * Strips non-numeric characters
     *
     * @param text dirty text to clean
     * @return String value of pennies
     */
    private String cleanText(final String text){
        return text.replaceAll("[^\\d]", "");
    }

    /**
     * Helper method for the overload version that takes a long
     *
     * @param pennies String representation of pennies
     * @return locale formatted double
     */
    private double penniesToCash(final String pennies){
        return penniesToCash(Long.valueOf(pennies));
    }

    /**
     * Converts penny values to user locale specific double
     *
     * E.G. in US, 2782 converted would be 27.82 since we use two decimal places for monetary representation
     * @param pennies pennies to convert
     * @return locale formatted double
     */
    private double penniesToCash(final long pennies){
        return pennies / Math.pow(10, numDecimals);
    }

    private String format(final double currency){
        return formatter.format(currency);
    }

    private void setCursor(final int pos){
        this.setSelection(pos);
    }


    final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            removeTextChangedListener(this);

            final String dirtyText = charSequence.toString();

            if(dirtyText.length() == 0)
                return;

            CurrencyEditText.this.pennies = Long.valueOf(cleanText(dirtyText));

            final double convertedPennies = penniesToCash(CurrencyEditText.this.pennies);

            final String cash = format(convertedPennies);

            setText(cash);

            setCursor(cash.length());

            if(listener != null)
                listener.onCurrencyChange(CurrencyEditText.this.pennies);

            addTextChangedListener(this);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };



    public void setOnCurrencyChangeListener(final OnCurrencyChange listener){
        this.listener = listener;
    }

    public interface OnCurrencyChange{
        void onCurrencyChange(final long pennies);
    }

}

