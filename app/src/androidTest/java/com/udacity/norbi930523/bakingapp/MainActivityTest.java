package com.udacity.norbi930523.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.norbi930523.bakingapp.activity.MainActivity;
import com.udacity.norbi930523.bakingapp.activity.RecipeDetailsActivity;
import com.udacity.norbi930523.bakingapp.model.Recipe;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by Norbert Boros on 2018. 03. 19..
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @BeforeClass
    public static void setup(){
        Intents.init();
    }

    @Before
    public void registerIdlingResource(){
        idlingResource = activityTestRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickOnRecipe_startsDetailsActivity() throws Exception {
        onView(withId(R.id.recipeListRecyclerView))
            .perform(actionOnItemAtPosition(0, click()));

        intended(allOf(
                hasComponent(RecipeDetailsActivity.class.getName()),
                hasExtra(equalTo(RecipeDetailsActivity.RECIPE_PARAM), instanceOf(Recipe.class))
        ));
    }

    public void unregisterIdlingResource(){
        if(idlingResource != null){
            IdlingRegistry.getInstance().unregister(idlingResource);
            idlingResource = null;
        }
    }

    @AfterClass
    public static void tearDown(){
        Intents.release();
    }

}
