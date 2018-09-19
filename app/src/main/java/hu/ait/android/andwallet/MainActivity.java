package hu.ait.android.andwallet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    public static final String KEY_INCOME = "KEY_INCOME";
    public static final String KEY_EXPENSE = "KEY_EXPENSE";
    public static final String KEY_BALANCE = "KEY_BALANCE";
    double income = 0;
    double expense = 0;
    double balance = 0;
    String ret;

    @BindView(R.id.btnDatePicker)
    Button btnDatePicker;
    @BindView(R.id.etItem)
    EditText etItem;
    @BindView(R.id.etAmount)
    PrefixEditText etAmount;
    @BindView(R.id.btnExpense)
    ToggleButton btnExpense;
    @BindView(R.id.tvBalanceText)
    TextView tvBalanceText;
    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ret = getString(R.string.text_balance) +
                getString(R.string.text_currency) + balance;
        tvBalanceText.setText(ret);
    }

    @OnClick(R.id.btnAdd)
    public void addItemButtonClicked() {
        if (!TextUtils.isEmpty(etItem.getText())) {
            if (!TextUtils.isEmpty(etAmount.getText())) {
                addExpenseRow();
            } else {
                etAmount.setError(getString(R.string.text_empty_field));
            }
        } else {
            etItem.setError(getString(R.string.text_empty_field));
        }
    }

    private void addExpenseRow() {
        // inflate one item row
        final View expenseRow = getLayoutInflater().inflate(
                R.layout.expense_row, null, false);

        // set the content of the item row
        setExpenseText(expenseRow);

        checkAmount(expenseRow);

        setUpDeleteBtn(expenseRow);

        // add item row to the main layout
        layoutContent.addView(expenseRow);
    }

    private void setExpenseText(View expenseRow) {
        TextView tvExpenseText = expenseRow.findViewById(R.id.tvExpenseText);
        TextView tvExpenseAmount = expenseRow.findViewById(R.id.tvExpenseAmount);
        TextView tvExpenseDate = expenseRow.findViewById(R.id.tvExpenseDate);

        tvExpenseText.setText(etItem.getText().toString());
        ret = getString(R.string.text_currency) + etAmount.getText().toString();
        tvExpenseAmount.setText(ret);
        tvExpenseDate.setText(btnDatePicker.getText());
    }

    private void checkAmount(View expenseRow) {
        ImageView ivIcon = expenseRow.findViewById(R.id.ivIcon);

        double amount = Double.parseDouble(etAmount.getText().toString());

        if (btnExpense.isChecked()) {
            ivIcon.setImageResource(R.drawable.income);
            ivIcon.setTag(getString(R.string.text_income));
            income += amount;
            balance += amount;
        } else {
            ivIcon.setImageResource(R.drawable.expense);
            ivIcon.setTag(getString(R.string.text_expense));
            expense += amount;
            balance -= amount;
        }

        ret = getString(R.string.text_balance) +
                getString(R.string.text_currency) + balance;
        tvBalanceText.setText(ret);
    }

    private void setUpDeleteBtn(final View expenseRow) {
        final Button btnDel = expenseRow.findViewById(R.id.btnDelTodo);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ivIcon = expenseRow.findViewById(R.id.ivIcon);
                TextView tvExpenseAmount = expenseRow.findViewById(R.id.tvExpenseAmount);
                double amount = Double.parseDouble(tvExpenseAmount.getText().
                        toString().substring(1));

                checkAmountOnDelete(ivIcon, amount);

                ret = getString(R.string.text_balance) +
                        getString(R.string.text_currency) + balance;
                tvBalanceText.setText(ret);
                layoutContent.removeView(expenseRow);
            }
        });
    }

    private void checkAmountOnDelete(ImageView ivIcon, double amount) {
        if (ivIcon.getTag() == getString(R.string.text_income)) {
            income -= amount;
            balance -= amount;
        } else {
            expense -= amount;
            balance += amount;
        }
    }

    @OnClick(R.id.btnClearAll)
    public void clearAllClicked() {
        layoutContent.removeAllViews();
        income = 0;
        expense = 0;
        balance = 0;
        ret = getString(R.string.text_balance) +
                getString(R.string.text_currency) + balance;
        tvBalanceText.setText(ret);
    }

    @OnClick(R.id.btnSummary)
    public void summaryClicked() {
        Intent summaryIntent = new Intent(this,
                SummaryActivity.class);
        summaryIntent.putExtra(KEY_INCOME, Double.toString(income));
        summaryIntent.putExtra(KEY_EXPENSE, Double.toString(expense));
        summaryIntent.putExtra(KEY_BALANCE, Double.toString(balance));
        setResult(RESULT_OK, summaryIntent);

        startActivity(summaryIntent);
    }

    @OnClick(R.id.btnDatePicker)
    public void datePickerClicked() {
        // calendar class's instance and get current btnDatePicker, month and year from calendar
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR); // current year
        int mMonth = calendar.get(Calendar.MONTH); // current month
        int mDay = calendar.get(Calendar.DAY_OF_MONTH); // current btnDatePicker
        // btnDatePicker picker dialog
        datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        ret = dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year;
                        btnDatePicker.setText(ret);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
