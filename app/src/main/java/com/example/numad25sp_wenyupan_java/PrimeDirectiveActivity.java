package com.example.numad25sp_wenyupan_java;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

public class PrimeDirectiveActivity extends AppCompatActivity {
    private static final String CONFIRM_EXIT_TITLE = "Exit Confirmation Required";
    private static final String CONFIRM_EXIT_MESSAGE = "Prime search is active. Do you still want to exit?";
    // Starting number for prime search is 3
    private static final int STARTING_PRIME_CANDIDATE = 3;
    // Initial prime number to display is 2
    private static final int INITIAL_PRIME_VALUE = 2;
    // Step increment for each prime check is 2
    private static final int PRIME_STEP_SIZE = 2;

    // UI components
    private Button stopSearchButton; // Button to stop searching for primes
    private Button startSearchButton; // Button to initiate prime search
    private CheckBox statusCheckBox; // Checkbox for additional settings
    private TextView primeResultView; // Displays the latest found prime
    private TextView candidateNumberView; // Displays the number being checked

    private Thread primeComputationThread; // Thread handling prime search
    private int candidateNumber; // Current number being evaluated
    private int latestPrime; // Most recently found prime number
    private boolean requiresReset; // Indicates whether search needs resetting
    private boolean computationRunning; // Tracks if search is ongoing
    private boolean priorSearchState; // Tracks if a search was running before interruption

    // Keys for saving instance state
    private static final String STATE_SEARCH_ONGOING = "searchOngoing";
    private static final String STATE_CANDIDATE_VALUE = "candidateValue";
    private static final String STATE_LAST_PRIME = "lastPrimeNumber";
    private static final String STATE_CHECKBOX_STATUS = "checkBoxState";
    private static final String STATE_RESET_FLAG = "resetFlag";

    // Determines if a number is prime
    private boolean checkIfPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_directive);
        Log.d("PrimeDirectiveActivity", "onCreate called. Restoring previous state if available.");
        initializeUIComponents();
        configureExitHandler();
        restorePreviousState(savedInstanceState);
        setupButtonActions();
    }

    // Restore saved instance state if available
    private void restorePreviousState(Bundle savedInstanceState) {
        requiresReset = true;

        if (savedInstanceState != null) {
            priorSearchState = savedInstanceState.getBoolean(STATE_SEARCH_ONGOING, false);
            candidateNumber = savedInstanceState.getInt(STATE_CANDIDATE_VALUE, STARTING_PRIME_CANDIDATE);
            latestPrime = savedInstanceState.getInt(STATE_LAST_PRIME, INITIAL_PRIME_VALUE);
            statusCheckBox.setChecked(savedInstanceState.getBoolean(STATE_CHECKBOX_STATUS, false));
            requiresReset = savedInstanceState.getBoolean(STATE_RESET_FLAG, true);

            primeResultView.setText("Latest Prime: " + latestPrime);
            candidateNumberView.setText("Checking: " + candidateNumber);

            if (priorSearchState) {
                startPrimeComputation();
            }
        }
        Log.d("PrimeDirectiveActivity", "Restoring state: searchOngoing=" + priorSearchState +
                ", candidateValue=" + candidateNumber + ", lastPrimeNumber=" + latestPrime);
    }

    // Starts searching for prime numbers
    private void startPrimeComputation() {
        Log.d("PrimeDirectiveActivity", "Starting prime computation...");
        if (computationRunning) {
            return;
        }
        priorSearchState = true;
        if (requiresReset) {
            candidateNumber = STARTING_PRIME_CANDIDATE;
            latestPrime = INITIAL_PRIME_VALUE;
            primeResultView.setText("Latest Prime: None");
            candidateNumberView.setText("Checking: " + STARTING_PRIME_CANDIDATE);
        }
        primeComputationThread = new Thread(() -> {
            while (priorSearchState) {
                runOnUiThread(() -> candidateNumberView.setText("Checking: " + candidateNumber));

                if (checkIfPrime(candidateNumber)) {
                    latestPrime = candidateNumber;
                    Log.d("PrimeDirectiveActivity", "New prime found: " + latestPrime);
                    runOnUiThread(() -> primeResultView.setText("Latest Prime: " + latestPrime));
                }
                // (For testing purposes, uncomment if needed)
                //try { Thread.sleep(1000); } catch (InterruptedException e) { return; }
                candidateNumber += PRIME_STEP_SIZE;
                Log.d("PrimeDirectiveActivity", "Checking next candidate: " + candidateNumber);

            }
        });
        computationRunning = true;
        primeComputationThread.start();
    }

    // Stops the prime computation process
    private void terminatePrimeComputation() {
        Log.d("PrimeDirectiveActivity", "Prime search terminated.");
        priorSearchState = false;
        requiresReset = true;
        computationRunning = false;
        if (primeComputationThread != null) {
            primeComputationThread.interrupt();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("PrimeDirectiveActivity", "Saving state: searchOngoing=" + priorSearchState +
                ", candidateValue=" + candidateNumber + ", lastPrimeNumber=" + latestPrime);
        super.onSaveInstanceState(outState);
        outState.putBoolean("searchOngoing", priorSearchState);
        outState.putInt("candidateValue", candidateNumber);
        outState.putInt("lastPrimeNumber", latestPrime);
        outState.putBoolean("checkBoxState", statusCheckBox.isChecked());
        outState.putBoolean("resetFlag", false);
    }

    // Initialize UI elements and buttons
    private void initializeUIComponents() {
        statusCheckBox = findViewById(R.id.pacifier_checkbox);
        startSearchButton = findViewById(R.id.find_primes_button);
        stopSearchButton = findViewById(R.id.terminate_search_button);
        primeResultView = findViewById(R.id.latest_prime);
        candidateNumberView = findViewById(R.id.current_number);
        Log.d("PrimeDirectiveActivity", "UI components initialized successfully.");
    }

    // Configure back press handling with exit confirmation
    private void configureExitHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (computationRunning) {
                    new AlertDialog.Builder(PrimeDirectiveActivity.this)
                            .setTitle(CONFIRM_EXIT_TITLE)
                            .setMessage(CONFIRM_EXIT_MESSAGE)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                terminatePrimeComputation();
                                finish();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    finish();
                }
            }
        });
    }

    // Setup button click listeners for user interaction
    private void setupButtonActions() {
        startSearchButton.setOnClickListener(v -> startPrimeComputation());
        stopSearchButton.setOnClickListener(v -> terminatePrimeComputation());
        Log.d("PrimeDirectiveActivity", "Button listeners have been set.");
    }
}
