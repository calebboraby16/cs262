// Caleb Boraby
// Homework01

package edu.calvin.cs262.ceb45.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // Private Variables
    private TextView result;
    private Spinner operator_selection;
    private EditText text_value1, text_value2;
    private String selected_operator;

    /**
     * @param savedInstanceState: bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button calculate;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set all interface objects to their private variables
        calculate = findViewById(R.id.Calculate_Button);
        result = findViewById(R.id.TextView_Result);
        operator_selection = findViewById(R.id.Operator_Spinner);
        text_value1 = findViewById(R.id.Value1_TextValue);
        text_value2 = findViewById(R.id.Value2_TextValue);

        // Sets the calculate button to listen
        calculate.setOnClickListener(this);
    }

    /**
     * No parameters
     */
    @Override
    protected void onStart() {

        // Invokes super class onStart()
        super.onStart();

        ArrayAdapter<CharSequence> char_adapter = ArrayAdapter.createFromResource(this, R.array.op_array, android.R.layout.simple_spinner_item);

        char_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Sets the spinner to the created adaptor
        operator_selection.setAdapter(char_adapter);

        // Sets the spinner to listen for a selected operator
        operator_selection.setOnItemSelectedListener(this);
    }

    /**
     *
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    // Takes the selected item and converts it to a string
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selected_operator = parent.getItemAtPosition(pos).toString();
    }

    /**
     * @param parent
     */
    // Handles nothing being selected for the spinner
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    /**
     * @param v
     */
    // Handles when the user clicks the calculate button
    @Override
    public void onClick(View v) {
        Log.d("Button", "Button Click Worked");
        Toast.makeText(this, "Calculating Result!", Toast.LENGTH_SHORT).show();

        String value_number1 = text_value1.getText().toString();
        Log.d("Button", "Value 1 allocated");
        String value_number2 = text_value2.getText().toString();
        Log.d("Button", "Value 2 allocated");

        // For some reason the if-statement didn't work so I tried a case-statement which ended ip working
        switch (selected_operator) {
            case "add":
                try {
                    int addition_result = Integer.parseInt(value_number1) + Integer.parseInt(value_number2);
                    result.setText(String.valueOf(addition_result));
                } catch (Exception e) {
                    result.setText("Invalid numbers!");
                }
                break;
            case "subtract":
                try {
                    int subtraction_result = Integer.parseInt(value_number1) - Integer.parseInt(value_number2);
                    result.setText(String.valueOf(subtraction_result));
                } catch (Exception e) {
                    result.setText("Invalid numbers!");
                }
                break;
            case "multiply":
                try {
                    int multiplication_result = Integer.parseInt(value_number1) * Integer.parseInt(value_number2);
                    result.setText(String.valueOf(multiplication_result));
                } catch (Exception e) {
                    result.setText("Invalid numbers!");
                }
                break;
            case "divide":
                try {
                    int division_result = Integer.parseInt(value_number1) / Integer.parseInt(value_number2);
                    result.setText(String.valueOf(division_result));
                } catch (Exception e) {
                    result.setText("Invalid numbers!");
                }
                break;
            default:
                break;
        }
        Log.d("Button", "Done with button click");
    }

}
