package hu.ait.android.andwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setAmountText();
    }

    private void setAmountText() {
        TextView incomeAmount = findViewById(R.id.incomeAmount);
        TextView expenseAmount = findViewById(R.id.expenseAmount);
        TextView balanceAmount = findViewById(R.id.balanceAmount);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String income = getString(R.string.text_currency) +
                    extras.getString(MainActivity.KEY_INCOME);
            String expense = getString(R.string.text_currency) +
                    extras.getString(MainActivity.KEY_EXPENSE);
            String balance = getString(R.string.text_currency) +
                    extras.getString(MainActivity.KEY_BALANCE);

            incomeAmount.setText(income);
            expenseAmount.setText(expense);
            balanceAmount.setText(balance);
        }
    }
}
