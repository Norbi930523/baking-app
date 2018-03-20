package com.udacity.norbi930523.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.norbi930523.bakingapp.activity.RecipeDetailsActivity;
import com.udacity.norbi930523.bakingapp.activity.RecipeStepActivity;
import com.udacity.norbi930523.bakingapp.model.Recipe;
import com.udacity.norbi930523.bakingapp.model.RecipeStep;
import com.udacity.norbi930523.bakingapp.util.NetworkUtil;
import com.udacity.norbi930523.bakingapp.util.RecipeJsonParserUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by Norbert Boros on 2018. 03. 19..
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule public ActivityTestRule<RecipeDetailsActivity> activityTestRule = new ActivityTestRule<>(RecipeDetailsActivity.class, true, false);

    private static List<Recipe> testRecipesList;

    private Recipe testRecipe;

    @BeforeClass
    public static void setup(){
        Intents.init();

        InputStream testRecipes = RecipeDetailsActivityTest.class.getClassLoader().getResourceAsStream("testRecipes.json");
        try {
            String json = IOUtils.toString(testRecipes, Charset.forName("UTF-8"));

            testRecipesList = RecipeJsonParserUtil.parseRecipeListJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void beforeEachTest(){
        testRecipe = testRecipesList.get(0);
        activityTestRule.launchActivity(createIntentWithRecipe(testRecipe));
    }

    public void afterEachTest(){
        activityTestRule.finishActivity();
    }

    @Test
    public void clickOnIngredientsStep_showsIngredients(){
        final int stepIndex = 0;

        onView(withId(R.id.recipeStepList))
                .perform(actionOnItemAtPosition(stepIndex, click()));

        if(activityTestRule.getActivity().isTwoPaneLayout()){
            onView(withId(R.id.ingredientsStepTitle)).check(matches(withText(R.string.ingredients)));
        } else {
            intended(allOf(
                    hasComponent(RecipeStepActivity.class.getName()),
                    hasExtra(equalTo(RecipeStepActivity.RECIPE_PARAM), equalTo(testRecipe)),
                    hasExtra(equalTo(RecipeStepActivity.SELECTED_STEP_INDEX_PARAM), equalTo(stepIndex))
            ));
        }

    }

    @Test
    public void clickOnMediaStep_showsVideoPlayer(){
        /* Find a step with a video url */
        final int mediaStepIndex = getRecipeStepWithVideo(testRecipe);

        /* Make assertions */
        onView(withId(R.id.recipeStepList))
                .perform(actionOnItemAtPosition(mediaStepIndex, click()));

        if(activityTestRule.getActivity().isTwoPaneLayout()){
            if(NetworkUtil.isOnline(activityTestRule.getActivity())){
                onView(withId(R.id.exoPlayerView)).check(matches(isDisplayed()));
            } else {
                onView(withId(R.id.offlineNoVideoMessage)).check(matches(isDisplayed()));
            }
        } else {
            intended(allOf(
                    hasComponent(RecipeStepActivity.class.getName()),
                    hasExtra(equalTo(RecipeStepActivity.RECIPE_PARAM), equalTo(testRecipe)),
                    hasExtra(equalTo(RecipeStepActivity.SELECTED_STEP_INDEX_PARAM), equalTo(mediaStepIndex))
            ));
        }

    }

    @Test
    public void clickOnSimpleStep_showsSimpleStepLayout(){
        /* Find a step without a video url */
        final int simpleStepIndex = getRecipeStepWithoutVideo(testRecipe);
        RecipeStep simpleStep = testRecipe.getSteps().get(simpleStepIndex - 1);

        /* Make assertions */
        onView(withId(R.id.recipeStepList))
                .perform(actionOnItemAtPosition(simpleStepIndex, click()));

        if(activityTestRule.getActivity().isTwoPaneLayout()){
            onView(withId(R.id.recipeStepDescription)).check(matches(withText(simpleStep.getDescription())));
        } else {
            intended(allOf(
                    hasComponent(RecipeStepActivity.class.getName()),
                    hasExtra(equalTo(RecipeStepActivity.RECIPE_PARAM), equalTo(testRecipe)),
                    hasExtra(equalTo(RecipeStepActivity.SELECTED_STEP_INDEX_PARAM), equalTo(simpleStepIndex))
            ));
        }

    }

    private int getRecipeStepWithVideo(Recipe recipe){
        for(int i = 0; i < recipe.getSteps().size(); i++){
            RecipeStep recipeStep = recipe.getSteps().get(i);

            if(StringUtils.isNotEmpty(recipeStep.getVideoURL()) ||
                    StringUtils.isNotEmpty(recipeStep.getThumbnailURL())){
                return i + 1; // add 1 to the index, because the ingredients step is prepended
            }
        }

        return -1;
    }

    private int getRecipeStepWithoutVideo(Recipe recipe){
        for(int i = 0; i < recipe.getSteps().size(); i++){
            RecipeStep recipeStep = recipe.getSteps().get(i);

            if(StringUtils.isEmpty(recipeStep.getVideoURL()) &&
                    StringUtils.isEmpty(recipeStep.getThumbnailURL())){
                return i + 1; // add 1 to the index, because the ingredients step is prepended
            }
        }

        return -1;
    }

    /* Based on http://blog.xebia.com/android-intent-extras-espresso-rules/ */
    private Intent createIntentWithRecipe(Recipe recipe) {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(targetContext, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.RECIPE_PARAM, recipe);
        return intent;
    }

    @AfterClass
    public static void tearDown(){
        Intents.release();
    }

}
