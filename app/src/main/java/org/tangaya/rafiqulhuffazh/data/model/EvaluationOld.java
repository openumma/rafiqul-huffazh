package org.tangaya.rafiqulhuffazh.data.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Environment;

import org.json.JSONObject;
import org.tangaya.rafiqulhuffazh.util.MurojaahEvaluator;
import org.tangaya.rafiqulhuffazh.util.QuranFactory;
import org.tangaya.rafiqulhuffazh.util.QuranScriptConverter;
import org.tangaya.rafiqulhuffazh.util.diff_match_patch;


public class EvaluationOld extends BaseObservable {

    String extStorageDir = Environment.getExternalStorageDirectory()+"";
    String quranVerseAudioDir = extStorageDir + "/rafiqul-huffazh";

    static int count = 0;

    public int id;

    // only store response with final transcription status
    //JSONObject mResponse;
    private ObservableField<JSONObject> mResponse = new ObservableField<>();

    //String mTranscription;
    private ObservableField<String> mTranscription = new ObservableField<>();

    private ObservableField<String> mArabicTranscription = new ObservableField<>();

    //String mVerseScript;
    private ObservableField<String> mVerseScript = new ObservableField<>();;

    //String mVerseQScript;
    private ObservableField<String> mVerseQScript = new ObservableField<>();

    //String mFilepath;
    private ObservableField<String> mFilepath = new ObservableField<>();

    //String verseNum;
    private ObservableField<String> verseNum = new ObservableField<>();

    //LinkedList<diff_match_patch.Diff> mDiff;
    //private ObservableField<LinkedList<diff_match_patch.Diff>> mDiff = new ObservableField<>();

    private ObservableField<String> mDiff = new ObservableField<>();

    private ObservableField<String> scoreStr = new ObservableField<>();

    private ObservableBoolean isCorrect = new ObservableBoolean();

    // Correct | Insertion | Deletion | Combination
    private ObservableField<String> evalStr = new ObservableField<>();

    //private int mChapter, mVerse, mSessionId;
    private ObservableInt mChapter = new ObservableInt();
    private ObservableInt mVerse = new ObservableInt();
    private ObservableInt mStatus = new ObservableInt();

    private int levenshteinValue;

    private ObservableInt earnedPoints = new ObservableInt();
    private ObservableInt maxPoints = new ObservableInt();

    private float score;

    // 0: unprocessed, 1: processing, 2: aborted, 3: finished (recognized)
    //private int mStatus;

    // todo: refactor to mvvm
    private diff_match_patch dmp = new diff_match_patch();

    public EvaluationOld(int chapter, int verse, int sessionId) {}

    Attempt attempt;
    String transcription, reference, strResult;
    public int chapter, verse;

    public EvaluationOld(Attempt attempt, String transcription) {
        this.attempt = attempt;
        this.transcription = transcription;

        chapter = attempt.getChapterNum();
        verse = attempt.getVerseNum();

        mChapter.set(chapter);
        mVerse.set(verse);

        verseNum.set("Verse " + verse);

        mVerseScript.set(QuranFactory.getAyah(mChapter.get(), mVerse.get()));
        mVerseQScript.set(QuranFactory.getQScript(mChapter.get(), mVerse.get()));

        isCorrect.set(false);

        notifyChange();

        reference = QuranFactory.getQScript(chapter, verse);
        isCorrect.set(transcription.equals(reference));

        if (isCorrect.get()) {
            strResult = "Correct";
        } else {
            strResult = MurojaahEvaluator.evaluate(attempt.getChapterNum(), attempt.getVerseNum(), transcription);
        }

        ////
        mTranscription.set(transcription);
        mVerseScript.set(QuranFactory.getAyah(chapter, verse));

        mVerseQScript.set(QuranFactory.getQScript(chapter, verse));

        mArabicTranscription.set(QuranScriptConverter.toArabic(transcription));

        mDiff.set(dmp.diff_main(mVerseQScript.get(), mTranscription.get()).toString());

        //levScore.set(dmp.diff_levenshtein(dmp.diff_main(mVerseQScript.get(), mTranscription.get())));

        levenshteinValue = MurojaahEvaluator.getLevenshteinDistance(mVerseQScript.get(), mTranscription.get());

        maxPoints.set(mVerseQScript.get().length());
        earnedPoints.set(maxPoints.get() - levenshteinValue);

        score = MurojaahEvaluator.getScore(mVerseQScript.get(), mTranscription.get());
        scoreStr.set(String.format("%.2f", score));

        if (mTranscription.get().equals(mVerseQScript.get())) {
            evalStr.set("Correct");
            scoreStr.set("1");
            isCorrect.set(true);
        } else {
            //evalStr.set("Wrong");   // todo: improve

            evalStr.set(MurojaahEvaluator.evaluate(mChapter.get(), mVerse.get(), transcription));
            isCorrect.set(false);
        }

        notifyChange();
        id = count++;
    }

    public ObservableInt getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus.set(status);
    }

    public void setFilepath(String filepath) {
        mFilepath.set(filepath);
    }

    public ObservableField<String> getTranscription() {
        return mTranscription;
    }

    public ObservableField<String> getArabicTranscription() {
        return mArabicTranscription;
    }

    public ObservableField<String> getVerseScript() {
        return mVerseScript;
    }

    public ObservableField<String> getVerseQScript() {
        return mVerseQScript;
    }

    public ObservableField<String> getDiff() {
        return mDiff;
    }

    public ObservableField<String> getVerseNum() {
        return verseNum;
    }

    public ObservableBoolean isCorrect() {
        return isCorrect;
    }

    public ObservableField<String> getEvalStr() {
        return evalStr;
    }

    public ObservableField<String> getScoreStr() {
        return scoreStr;
    }

    public float getScore() {
        return score;
    }

    public ObservableInt getMaxpoints() {
        return maxPoints;
    }

    public ObservableInt getEarnedPoints() {
        return earnedPoints;
    }
}
