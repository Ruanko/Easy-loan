package com.ruanko.easyloanadmin.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.ruanko.easyloanadmin.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView mUserNameView;
    private TextInputLayout inputUsername, inputPassword;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button login_button;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_forgot_password:
                attemptResetPassword();
                break;
            case R.id.btn_register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void initView() {
        mLoginFormView = findViewById(R.id.form_login);
        mProgressView = findViewById(R.id.progress_login);
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.tv_user_name);
        mPasswordView = (EditText) findViewById(R.id.tv_password);
        inputUsername = (TextInputLayout) findViewById(R.id.input_user_name);
        inputPassword = (TextInputLayout) findViewById(R.id.input_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        login_button = (Button) findViewById(R.id.btn_login);
        login_button.setOnClickListener(this);
        Button forgot_password = (Button) findViewById(R.id.btn_forgot_password);
        forgot_password.setOnClickListener(this);
        Button register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        Button resetPassword = (Button) findViewById(R.id.btn_forgot_password);
        resetPassword.setOnClickListener(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form errors
     * (invalid email, missing fields, etc.), the errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (false) {
            return;
        }

        // Reset errors.
        inputUsername.setError(null);
        inputPassword.setError(null);

        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            inputUsername.setError(getString(R.string.error_no_name));
            focusView = mUserNameView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if ((isPhoneValid(username) || isEmailValid(username)) && TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_no_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            hideInput(login_button);
            showProgress(true);
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        showProgress(false);
                        String json = e.getMessage();
                        JSONTokener tokener = new JSONTokener(json);
                        try{
                            JSONObject jsonObject = (JSONObject) tokener.nextValue();
                            Toast.makeText(LoginActivity.this,
                                    jsonObject.getString("error"),
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (JSONException jse) {
                            jse.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    private boolean isPhoneValid(String userName) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(userName);
        return m.matches() && userName.length() >= 7 && userName.length() <= 12;
    }

    private boolean isEmailValid(String userName) {
        return userName.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4 && password.length() <= 20;
    }

    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void attemptResetPassword() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
        View dialogView = LoginActivity.this.getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        Button sendEmail = (Button) dialogView.findViewById(R.id.btn_send_reset_email);
        bottomSheetDialog.setContentView(dialogView);
        final EditText emailTextView = (EditText) dialogView.findViewById(R.id.tv_reset_password);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (emailTextView.getText().length() ==)
                AVUser.requestPasswordResetInBackground(emailTextView.getText().toString(),
                        new RequestPasswordResetCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "密码重置邮件已经发到您的邮箱", Toast.LENGTH_LONG).show();
                        } else {
                            e.printStackTrace();
                            String json = e.getMessage();
                            JSONTokener tokener = new JSONTokener(json);
                            try{
                                JSONObject jsonObject = (JSONObject) tokener.nextValue();
                                Toast.makeText(LoginActivity.this,
                                        jsonObject.getString("error"),
                                        Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException jse) {
                                jse.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        bottomSheetDialog.show();
        emailTextView.requestFocus();
    }
}
