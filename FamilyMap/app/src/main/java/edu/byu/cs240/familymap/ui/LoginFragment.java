package edu.byu.cs240.familymap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs240.familymap.R;
import edu.byu.cs240.familymap.model.DataCache;
import model.Event;

/**
 * Login fragment for the main activity
 */
public class LoginFragment extends Fragment {
    private static final String LOGIN_TASK_RESULT_KEY = "LoginTaskResultKey";
    private static final String REGISTER_TASK_RESULT_KEY = "RegisterTaskResultKey";
    private static final String LOGGED_IN = "LogInIntent";

    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioGroup genderButtons;
    private Button loginButton;
    private Button registerButton;

    public LoginFragment() {
    }

    /**
     * Creates new instance of login fragment
     *
     * @return login fragment
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new login fragment and gets rid of the
     * options menu
     *
     * @param savedInstanceState bundle holding arguments
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
    }

    /**
     * Creates widgets and listeners for login fragment
     *
     * @param inflater inflater for views
     * @param container view context for views
     * @param savedInstanceState bundle holding arguments
     * @return overall view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);


        serverHostEditText = (EditText)v.findViewById(R.id.serverHostEditText);
        serverHostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (serverHostEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                    loginButton.setEnabled(false);
                } else {
                    if (serverPortEditText.getText().length() != 0 && userNameEditText.getText().length() != 0
                            && passwordEditText.getText().length() != 0) {
                        loginButton.setEnabled(true);
                        if (firstNameEditText.getText().length() != 0 && lastNameEditText.getText().length() != 0
                                && emailEditText.getText().length() != 0 && genderButtons.getCheckedRadioButtonId() != -1) {
                            registerButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        serverPortEditText = (EditText)v.findViewById(R.id.serverPortEditText);
        serverPortEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (serverPortEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                    loginButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && userNameEditText.getText().length() != 0
                            && passwordEditText.getText().length() != 0) {
                        loginButton.setEnabled(true);
                        if (firstNameEditText.getText().length() != 0 && lastNameEditText.getText().length() != 0
                                && emailEditText.getText().length() != 0 && genderButtons.getCheckedRadioButtonId() != -1) {
                            registerButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        userNameEditText = (EditText)v.findViewById(R.id.userNameEditText);
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (userNameEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                    loginButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                            && passwordEditText.getText().length() != 0) {
                        loginButton.setEnabled(true);
                        if (firstNameEditText.getText().length() != 0 && lastNameEditText.getText().length() != 0
                                && emailEditText.getText().length() != 0 && genderButtons.getCheckedRadioButtonId() != -1) {
                            registerButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        passwordEditText = (EditText)v.findViewById(R.id.passwordEditText);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                    loginButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                            && userNameEditText.getText().length() != 0) {
                        loginButton.setEnabled(true);
                        if (firstNameEditText.getText().length() != 0 && lastNameEditText.getText().length() != 0
                                && emailEditText.getText().length() != 0 && genderButtons.getCheckedRadioButtonId() != -1) {
                            registerButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        firstNameEditText = (EditText)v.findViewById(R.id.firstNameEditText);
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (firstNameEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                            && userNameEditText.getText().length() != 0 && passwordEditText.getText().length() != 0
                            && lastNameEditText.getText().length() != 0 && emailEditText.getText().length() != 0
                            && genderButtons.getCheckedRadioButtonId() != -1) {
                        registerButton.setEnabled(true);
                    }
                }
            }
        });

        lastNameEditText = (EditText)v.findViewById(R.id.lastNameEditText);
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (lastNameEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                            && userNameEditText.getText().length() != 0 && passwordEditText.getText().length() != 0
                            && firstNameEditText.getText().length() != 0 && emailEditText.getText().length() != 0
                            && genderButtons.getCheckedRadioButtonId() != -1) {
                        registerButton.setEnabled(true);
                    }
                }
            }
        });

        emailEditText = (EditText)v.findViewById(R.id.emailEditText);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (emailEditText.getText().length() == 0) {
                    registerButton.setEnabled(false);
                } else {
                    if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                            && userNameEditText.getText().length() != 0 && passwordEditText.getText().length() != 0
                            && lastNameEditText.getText().length() != 0 && firstNameEditText.getText().length() != 0
                            && genderButtons.getCheckedRadioButtonId() != -1) {
                        registerButton.setEnabled(true);
                    }
                }
            }
        });

        genderButtons = (RadioGroup)v.findViewById(R.id.genderButtons);
        genderButtons.setOnCheckedChangeListener((group, checkedId) -> {
            if (genderButtons.getCheckedRadioButtonId() == -1) {
                registerButton.setEnabled(false);
            } else {
                if (serverHostEditText.getText().length() != 0 && serverPortEditText.getText().length() != 0
                        && userNameEditText.getText().length() != 0 && passwordEditText.getText().length() != 0
                        && lastNameEditText.getText().length() != 0 && firstNameEditText.getText().length() != 0
                        && emailEditText.getText().length() != 0) {
                    registerButton.setEnabled(true);
                }
            }
        });



        loginButton = (Button)v.findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(v1 -> {

            Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    String endResult = bundle.getString(LOGIN_TASK_RESULT_KEY);
                    Toast.makeText(getContext(), endResult, Toast.LENGTH_LONG).show();
                    if (!endResult.equals("Login failed.")) {
                        System.out.println("Login Succeeded");


                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(LOGGED_IN, true);
                        startActivity(intent);

                    }
                }
            };

            LoginAsyncTask loginTask = new LoginAsyncTask(uiThreadMessageHandler, serverHostEditText.getText().toString(),
                    serverPortEditText.getText().toString(), userNameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(loginTask);
        });

        registerButton = (Button)v.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(v12 -> {
            Handler uiThreadMessageHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    String endResult = bundle.getString(REGISTER_TASK_RESULT_KEY);
                    Toast.makeText(getContext(), endResult, Toast.LENGTH_LONG).show();
                    if (!endResult.equals("Registration failed.")) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(LOGGED_IN, true);
                        startActivity(intent);
                    }
                }
            };

            RegisterAsyncTask registerTask = new RegisterAsyncTask(uiThreadMessageHandler, serverHostEditText.getText().toString(),
                    serverPortEditText.getText().toString(), userNameEditText.getText().toString(),
                    passwordEditText.getText().toString(), firstNameEditText.getText().toString(),
                    lastNameEditText.getText().toString(), emailEditText.getText().toString(),
                    genderButtons.getCheckedRadioButtonId() == R.id.maleGenderButton);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(registerTask);
        });

        return v;
    }
}