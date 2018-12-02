package currencyedittext.whompum.com.currencyedittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

/**
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

public class CurrencyEditText extends AppCompatEditText{

    private CurrencyFormatter formatter = CurrencyFormatter.getInstance();

    public CurrencyEditText(@NonNull final Context context){
        super( context );
        init();
    }

    public CurrencyEditText(@NonNull final Context context, @Nullable final AttributeSet set){
        super( context, set );
        init();
    }

    public CurrencyEditText(@NonNull final Context context, @Nullable AttributeSet attrs, final int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init();
    }

    private void init(){
        addTextChangedListener( textWatcher );

        if( getText() != null && !TextUtils.isEmpty( getText().toString() ) )
            setText( getText().toString() );
        else
            setText( "0" );

        setMovementMethod( null );
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
     * Sets the cursor selection to the far Right (far left in RTL) because
     * values are added from the end-start.
     * @param pos far right (left 4 RTL) position
     */
    private void setCursor(final int pos){
        this.setSelection(pos);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            //Setting text in this method, so remove the watcher to avoid infinite recursion.
            removeTextChangedListener( this );

            final String dirtyText = charSequence.toString();

            if( dirtyText.length() == 0 )
                return;

            final String cash = formatter.convert( Long.valueOf( cleanText( dirtyText ) ) );

            setText( cash );

            setCursor( cash.length() );

            addTextChangedListener( this );

    }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) { }

    };


}

