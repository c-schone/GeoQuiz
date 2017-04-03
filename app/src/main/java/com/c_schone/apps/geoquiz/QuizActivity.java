package com.c_schone.apps.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String ANSWER_INDEX = "answers";
    private static final String ANSWER_COUNTER = "questionsAnswered";
    private static final String CORRECT_COUNTER = "correctAnswers";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String IS_CHEATING = "hasCheated";
    private static final String CHEAT_COUNT = "cheatsCounted";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    /**
     * Challenge Chapter 2
     * private ImageButton mNextImageButton;
     * private ImageButton mPrevImageButton;
     */
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    /**
     * Chapter 3 Challenge 1
     */
    private boolean[] mAnswered = new boolean[] {
            false, false, false, false, false, false
    };

    /**
     * Chapter 5 Challenge
     */
    private boolean[] mCheated = new boolean[] {
            false, false, false, false, false, false
    };

    /**
     * Chapter 3 Challenge 2
     */
    private int mCorrectAnswered = 0;
    private int mQuestionsAnswered = 0;

    private int mCurrentIndex = 0;
    //deprecated
    //private boolean mIsCheater;

    /**
     * Chapter 6 Challenge 2
     */
    private int mCheatCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswered = savedInstanceState.getBooleanArray(ANSWER_INDEX);
            mQuestionsAnswered = savedInstanceState.getInt(ANSWER_COUNTER, 0);
            mCorrectAnswered = savedInstanceState.getInt(CORRECT_COUNTER, 0);
            mCheated = savedInstanceState.getBooleanArray(IS_CHEATING);
            mCheatCounter = savedInstanceState.getInt(CHEAT_COUNT, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        /**
         * Chapter 2 Challenge 1: Add Listener to the TextView
         *
         * mQuestionTextView.setOnClickListener(new View.OnClickListener() {
         *  @Override
         *  public void onClick(View v) {
         *      nextQuestion(1);
         *  }
         * });
         */

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Challenge Chapter 1: Customizing the Toast
                 *
                 * Toast mToast = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
                 * mToast.setGravity(Gravity.TOP, 0, 0);
                 * mToast.show();
                 */
                //deprecated
                //Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               /**
                * Challenge Chapter 1: Customizing the Toast
                *
                * Toast mToast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
                * mToast.setGravity(Gravity.TOP, 0, 0);
                * mToast.show();
                */
               //deprecated
               //Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
               checkAnswer(false);
           }
        });

        /**
         * Chapter 2 Challenge 2: Add a Previous Button
         */
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion(-1);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion(1);
            }
        });

        /**
         * Chapter 2 Challenge 3: From Button to ImageButton
         *
         * mPrevImageButton = (ImageButton) findViewById(R.id.prev_image_button);
         * mPrevImageButton.setOnClickListener(new View.OnClickListener() {
         * @Override
         * public void onClick(View v) {
         * nextQuestion(false);
         * }
         * });
         *
         * mNextImageButton = (ImageButton) findViewById(R.id.next_image_button);
         * mNextImageButton.setOnClickListener(new View.OnClickListener() {
         * @Override
         * public void onClick(View v) {
         * nextQuestion(true);
         * }
         * });
         */

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            boolean temp = CheatActivity.wasAnswerShown(data);
            mCheated[mCurrentIndex] = temp;
            if (temp) {
                mCheatCounter++;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(ANSWER_INDEX, mAnswered);
        savedInstanceState.putInt(ANSWER_COUNTER, mQuestionsAnswered);
        savedInstanceState.putInt(CORRECT_COUNTER, mCorrectAnswered);
        savedInstanceState.putBooleanArray(IS_CHEATING, mCheated);
        savedInstanceState.putInt(CHEAT_COUNT, mCheatCounter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        buttonEnabler();
    }

    private void checkAnswer (boolean userPressedTrue) {
        mQuestionsAnswered++;
        boolean isTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mCheated[mCurrentIndex]) {
            messageResId = R.string.judgment_toast;
        } else {
            if (isTrue == userPressedTrue) {
                mCorrectAnswered++;
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        /**
         * Disable answer buttons
         */
        mAnswered[mCurrentIndex] = true;
        buttonEnabler();

        /**
         * Chapter 3 Challenge 2
         */
        if (mQuestionsAnswered == 6) {
            Log.d(TAG, "mCorrect: " + mCorrectAnswered + ", mQuestionsAnswered: " + mQuestionsAnswered);
            Toast.makeText(this, "Correct answers: " + mCorrectAnswered * 100 / mQuestionsAnswered + "%", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Chapter 2 Challenges
     * Help function
     * Iterates forward or backward depending on next
     */
    private void nextQuestion(int next) {
        mCurrentIndex = ((mCurrentIndex + next) + mQuestionBank.length) % mQuestionBank.length;
        //deprecated
        //mIsCheater = false;
        updateQuestion();
    }

    /**
     * Chapter 3 Challenge + Chapter 6 Challenge 2
     * Help function
     * Enables or disables buttons for answering and cheating
     */
    private void buttonEnabler() {
        if (mAnswered[mCurrentIndex]) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
        if (mCheatCounter == 3) {
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
    }
}
