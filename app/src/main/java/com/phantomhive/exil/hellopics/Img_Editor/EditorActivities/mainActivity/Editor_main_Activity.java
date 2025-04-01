package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings.PasswordSettings;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.AddPhotoActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.AdjustImageActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.CloneActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.CropActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.DrawingActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.EffectsActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.FreeCropActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.GradientActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.ImageBorderActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.RespectiveCropActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.RotateImageActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.ScaleActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.Select_crop_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.StretchActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.TextOnImageActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings.Item;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.tools_name;
import com.phantomhive.exil.hellopics.Img_Editor.Views.MainImgZoomView.MainImgZoomView;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Editing_tools;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Tools_Name;
import com.phantomhive.exil.hellopics.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Editor_main_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //main ImageView.
    MainImgZoomView mainImgZoomView;
    //constraintLayout for Resize imageView.
    ConstraintLayout constraintLayout;
    // RecyclerView for tools.
    RecyclerView recyclerView_tools;
    //Adapter for recyclerView_tools
    Tools_home_Adapter tools_home_adapter;
    //list of Tools.
    ArrayList<Editing_tools> editingtools;

    ArrayList<Tools_Name> _tools_names;
    Image image;
    // Decide Button.
    ImageButton imageButtonDone, imageButtonBack, imageButtonHistory;

    boolean a = false, b = false;
    boolean On = true;

    Dialog myDialog;
    EditText ImgName;
    Spinner spino;
    SeekBar qualitySeekbar;
    TextView imageQualityValue;
    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
    String stringformat = ".png";
    int imgQuality = 90;


    private InterstitialAd mInterstitialAd;
    private final String TAG = "MainActivity";
    private boolean isAdLoading = false; // Prevent multiple loads

    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_main);
        EdgeToEdgeFixing(R.id.Editor_main_ActivityL,this);

        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(this, initializationStatus -> {
        });
        // Load the ad
        loadAd();

        //findViewById.
        mainImgZoomView = findViewById(R.id.editedimage);
        constraintLayout = findViewById(R.id.constraint_Scaler);
        recyclerView_tools = findViewById(R.id.RecycleView_tools_item);
        imageButtonDone = findViewById(R.id.imageButton_Done);
        imageButtonBack = findViewById(R.id.imageButton_Back);
        imageButtonHistory = findViewById(R.id.imageButtonDownload);


        image = Image.getInstance();

        mainImgZoomView.setZoomCase(true);


        if (image.getBitmap() == null) {
            Toast.makeText(Editor_main_Activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        } else {
            mainImgZoomView.setImageBitmap(image.getBitmap());
        }


        //close Editor.
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Editor_main_Activity.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to proceed?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle confirm action
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Close the dialog
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        myDialog = new Dialog(Editor_main_Activity.this);
        myDialog.setContentView(R.layout.popoutlayout);

        ImgName = myDialog.findViewById(R.id.ImgName);
        spino = myDialog.findViewById(R.id.Formatsspinner);
        qualitySeekbar = myDialog.findViewById(R.id.qualitySeekbar);
        imageQualityValue = myDialog.findViewById(R.id.ImageQuality);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spino.setAdapter(adapter);

        spino.setOnItemSelectedListener(Editor_main_Activity.this);

        qualitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imgQuality = progress * 10;
                imageQualityValue.setText("Quality:" + imgQuality + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button phoneSave = myDialog.findViewById(R.id.phoneSave);
        Button AppSave = myDialog.findViewById(R.id.AppSave);
        Button HiddenSecretsSave = myDialog.findViewById(R.id.HideSave);

        ProgressBar progressBar = myDialog.findViewById(R.id.SavingImageprogressBar);
        ImageButton Close = myDialog.findViewById(R.id.close);

        // done from editing Image.
        imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Ad was dismissed, now start the editor
                            ShowSaveImageBar();
                            Log.d(TAG, "Ad dismissed, starting editor.");
                            // Load a new ad for the next time
                            loadAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            mInterstitialAd = null;
                            Log.d(TAG, "Ad failed to show: " + adError.getMessage());
                            ShowSaveImageBar();
                            loadAd();
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.");
                            // Load a new ad for the next time
                            loadAd();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.");
                            // Load a new ad for the next time
                            loadAd();
                        }
                    });

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(Editor_main_Activity.this);
                        loadAd();
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        loadAd();
                    }

                } else {
                    // If no ad is available, start the editor immediately
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                    ShowSaveImageBar();
                    loadAd(); // Load a new ad
                }
            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        phoneSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImage(image.getBitmap(),progressBar);
            }
        });


        HiddenSecretsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordSettings passwordSettings = new PasswordSettings(Editor_main_Activity.this);
                try {
                if (passwordSettings.getPassword()==null){
                    Toast.makeText(Editor_main_Activity.this, "Start Hidden Secrets First", Toast.LENGTH_SHORT).show();
                }else {
                    hideImage(image.getBitmap(),Editor_main_Activity.this,progressBar);
                }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });


        AppSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveImageOnApp(image.getBitmap(),Editor_main_Activity.this,progressBar);


            }
        });


        //Download Image.
        imageButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Editor_main_Activity.this);
            }
        });


        //set the editing Tools Arraylist and set adapter.
        editingtools = ToolsHome();
        if (editingtools.isEmpty()) {
            Log.d("Tools_Home_Arraylist", "Arraylist is empty");
        } else {
            tools_home_adapter = new Tools_home_Adapter(editingtools, Editor_main_Activity.this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Editor_main_Activity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView_tools.setLayoutManager(horizontalLayoutManager);
            recyclerView_tools.setAdapter(tools_home_adapter);
        }
    }


    public void ShowSaveImageBar(){
        myDialog.show();
        Random generator = new Random();

        int n = 1000000;
        n = generator.nextInt(n);
        String fileName = "Image-" + n;//+".jpg"

        if (image.getBitmap().hasAlpha()) {
            spino.setSelection(1);
        } else {
            spino.setSelection(0);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("imgname") != null) {
                ImgName.setText(extras.getString("imgname"));
            } else {
                ImgName.setText(fileName);
            }
        } else {
            ImgName.setText(fileName);
        }
    }
    private void loadAd() {
        if (isAdLoading || mInterstitialAd != null) {
            return; // Prevent multiple simultaneous loads
        }

        isAdLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(this,"ca-app-pub-9200222703965948/7286644839", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        isAdLoading = false; // Ad is ready
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                        isAdLoading = false; // Allow future loads
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If the ad is null, load a new one
        if (mInterstitialAd == null) {
            loadAd();
        }
    }


    // Fill the ArrayList of editing tools.
    @SuppressLint("UseCompatLoadingForDrawables")
    public ArrayList<Editing_tools> ToolsHome() {
        ArrayList<Editing_tools> editingtools = new ArrayList<>();
        editingtools.add(new Editing_tools(tools_name.TOOLS, getResources().getDrawable(R.drawable.tools_ic, null)));
        editingtools.add(new Editing_tools(tools_name.ADJUST, getResources().getDrawable(R.drawable.adjust_ic, null)));
        editingtools.add(new Editing_tools(tools_name.TEXT, getResources().getDrawable(R.drawable.text_ic, null)));
        editingtools.add(new Editing_tools(tools_name.Add_Image, getResources().getDrawable(R.drawable.add_image_ic, null)));
        editingtools.add(new Editing_tools(tools_name.BORDER, getResources().getDrawable(R.drawable.border_ic, null)));
        editingtools.add(new Editing_tools(tools_name.RESIZE, getResources().getDrawable(R.drawable.scale_hw, null)));
        editingtools.add(new Editing_tools(tools_name.DRAW, getResources().getDrawable(R.drawable.draw_ic, null)));
        editingtools.add(new Editing_tools(tools_name.GRADIENT, getResources().getDrawable(R.drawable.gradient_ic, null)));
        editingtools.add(new Editing_tools(tools_name.EFFECT, getResources().getDrawable(R.drawable.photo_filter_ic, null)));
        return editingtools;
    }

    public void ShowImageResizeDialogue() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enter Details");

        // Create the layout for the AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(50, 50, 50, 50);


        // Create the TextView and EditText for the first input
        TextView textView1 = new TextView(this);
        textView1.setText("Width:");
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(16);
        layout.addView(textView1);

        // Create the EditText fields
        TextInputEditText editText1 = new TextInputEditText(this);
        editText1.setHint("Width");
        editText1.setGravity(Gravity.CENTER);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText1.setText(String.valueOf(image.getBitmap().getWidth()));
        layout.addView(editText1);

        // Create the ImageView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.relation_ic);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setPadding(0, 20, 0, 20);
        layout.addView(imageView);


        // Create the TextView and EditText for the second input
        TextView textView2 = new TextView(this);
        textView2.setText("Height:");
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(16);
        layout.addView(textView2);

        TextInputEditText editText2 = new TextInputEditText(this);
        editText2.setHint("Height");
        editText2.setGravity(Gravity.CENTER);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText2.setText(String.valueOf(image.getBitmap().getHeight()));
        layout.addView(editText2);


        // Set the layout to the AlertDialog
        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Width = editText1.getText().toString();
                String Height = editText2.getText().toString();

                // Do something with the entered text
                // For example, display a Toast message


                if (!Width.isEmpty() && !Height.isEmpty()) {
                    int w = Integer.parseInt(Width);
                    int h = Integer.parseInt(Height);
                    if (h != 0 && w != 0) {
                        int pixels = ((w * h) / 1000000);
                        if (pixels < 10) {
                            Bitmap bmd = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                            Bitmap bitmap = Bitmap.createScaledBitmap(image.getBitmap(), w
                                    , h, true);
                            Canvas canvas = new Canvas(bmd);
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setAntiAlias(true);
                            paint.setDither(true);
                            paint.setFilterBitmap(true);
                            canvas.drawBitmap(bitmap, 0, 0, paint);
                            image.setBitmap(bmd);
                            mainImgZoomView.setImageBitmap(image.getBitmap());
                            On = true;
                        } else {
                            int totalPixels = 10000000;

                            double aspectRatio = (double) w / h;

                            int widthMax = (int) Math.sqrt(totalPixels * aspectRatio);
                            int heightMax = (int) (widthMax / aspectRatio);

                            Toast.makeText(Editor_main_Activity.this,
                                    "Your max H:W are : (" + widthMax + "*" + heightMax + ")", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(Editor_main_Activity.this, "The value of H:W should be more then 0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Editor_main_Activity.this, "The value of H:W should be more then 0", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                On = true;
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!b) {
                        String Width = editText1.getText().toString();
                        int w = Integer.parseInt(Width);
                        if (w != 0) {
                            a = true;
                            float division = ((float) (image.getBitmap().getWidth()) / (float) (image.getBitmap().getHeight()));
                            float newH = w / division;
                            editText2.setText(String.valueOf((int) newH));
                        } else {
                            editText2.setText(String.valueOf(1));
                        }

                    }
                } catch (NumberFormatException ex) {
                    editText2.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                a = false;
            }
        };

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!a) {
                        b = true;
                        String Height = editText2.getText().toString();
                        int h = Integer.parseInt(Height);
                        if (h != 0) {
                            float division1 = ((float) (image.getBitmap().getHeight()) / (float) (image.getBitmap().getWidth()));
                            float newW = h / division1;
                            editText1.setText(String.valueOf((int) newW));
                        } else {
                            editText1.setText(String.valueOf(1));
                        }

                    }
                } catch (NumberFormatException ex) {
                    editText1.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                b = false;
            }
        };


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                On = !On;
                if (!On) {
                    imageView.setImageResource(R.drawable.close_ic);
                    editText1.removeTextChangedListener(watcher);
                    editText2.removeTextChangedListener(watcher2);
                } else {
                    imageView.setImageResource(R.drawable.relation_ic);
                    editText1.addTextChangedListener(watcher);
                    editText2.addTextChangedListener(watcher2);
                }
            }
        });
        if (!On) {
            editText1.removeTextChangedListener(watcher);
            editText2.removeTextChangedListener(watcher2);
        } else {
            editText1.addTextChangedListener(watcher);
            editText2.addTextChangedListener(watcher2);
        }
    }


    // Fill the Arraylist of tools.
    @SuppressLint("UseCompatLoadingForDrawables")
    public ArrayList<Tools_Name> reale_tools_names() {
        ArrayList<Tools_Name> tools_names = new ArrayList<>();
        tools_names.add(new Tools_Name(tools_name.CROP, getResources().getDrawable(R.drawable.crop_ic, null)));
        tools_names.add(new Tools_Name(tools_name.SELECT, getResources().getDrawable(R.drawable.select_ic, null)));
        tools_names.add(new Tools_Name(tools_name.FREE_CROP, getResources().getDrawable(R.drawable.cut_ic, null)));
        tools_names.add(new Tools_Name(tools_name.ROTATE, getResources().getDrawable(R.drawable.rotate_left_ic, null)));
        tools_names.add(new Tools_Name(tools_name.PERSPECTIVE, getResources().getDrawable(R.drawable.perspective_ic, null)));
        //tools_names.add(new Tools_Name(tools_name.STRETCH,getResources().getDrawable(R.drawable.stretch_ic,null)));
        tools_names.add(new Tools_Name(tools_name.PERSPECTIVE_CROP, getResources().getDrawable(R.drawable.perspective_crop_ic, null)));
        tools_names.add(new Tools_Name(tools_name.CLONE, getResources().getDrawable(R.drawable.clone_ic, null)));
        return tools_names;
    }


    //Designed Alert Dialog For Selection the Tools
    public void ShowRToolsDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Editor_main_Activity.this);
        bottomSheetDialog.setContentView(R.layout.tools_h_dialog);
        //AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        // View view = LayoutInflater.from(this).inflate(R.layout.tools_h_dialog,null,false);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.dialogToolsRecycle);
        DialiogToolsAdapter dialiogToolsAdapter;
        _tools_names = reale_tools_names();
        if (_tools_names.isEmpty()) {
            Log.d("Tools_Home_Arraylist", "Arraylist is empty");
        } else {
            dialiogToolsAdapter = new DialiogToolsAdapter(this, _tools_names,bottomSheetDialog);
            assert recyclerView != null;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            recyclerView.setAdapter(dialiogToolsAdapter);
        }
        bottomSheetDialog.show();
    }



    /*
     public static Activity fa;
     {
        fa = this;
    }
     */


    private void SaveImageOnApp(Bitmap finalBitmap, Context context, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        new Thread(() -> {
            // Define the hidden directory
            File hiddenDir = new File(context.getFilesDir(), "TheInsideAlbumHelloPics");
            if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

            // Generate the file name
            String fileName = ImgName.getText().toString().trim();
            File file = new File(hiddenDir, fileName);

            if (file.exists()) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    Toast.makeText(context, "This name is already used", Toast.LENGTH_SHORT).show();
                });
            } else {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    // Use the same compression format and quality
                    finalBitmap.compress(compressFormat, imgQuality, out);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Notify the system that a new file has been created
                MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                        (path, uri) -> {
                            Log.i("HiddenStorage", "Scanned " + path + ":");
                            Log.i("HiddenStorage", "-> uri=" + uri);
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                                Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show();
                            });
                        });
            }
        }).start();
    }

    private void SaveImageOnApp(Bitmap finalBitmap, Context context) {
        // Define the hidden directory
        File hiddenDir = new File(context.getFilesDir(), "TheInsideAlbumHelloPics");
        if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

        // Generate the file name
        String fileName = ImgName.getText().toString().trim();
        File file = new File(hiddenDir, fileName);

        if (file.exists()) {
            Toast.makeText(context, "This name is already used", Toast.LENGTH_SHORT).show();
        } else {
            try (FileOutputStream out = new FileOutputStream(file)) {
                // Use the same compression format and quality
                finalBitmap.compress(compressFormat, imgQuality, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Notify the system that a new file has been created
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("HiddenStorage", "Scanned " + path + ":");
                            Log.i("HiddenStorage", "-> uri=" + uri);
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(context, "Image saved successfully",
                                            Toast.LENGTH_SHORT).show());
                        }
                    });
        }
    }

    private void hideImage(Bitmap finalBitmap, Context context,ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        new Thread(() -> {
            // Define the hidden directory
            File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics");
            if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

            // Generate the file name
            String fileName = ImgName.getText().toString().trim() + stringformat;
            File file = new File(hiddenDir, fileName);

            if (file.exists()) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    Toast.makeText(context, "This name is already used", Toast.LENGTH_SHORT).show();
                });
            } else {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    // Use the same compression format and quality
                    finalBitmap.compress(compressFormat, imgQuality, out);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        Toast.makeText(context, "Failed to hide image", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Notify the system that a new file has been created
                MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                        (path, uri) -> {
                            Log.i("HiddenStorage", "Scanned " + path + ":");
                            Log.i("HiddenStorage", "-> uri=" + uri);
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                                Toast.makeText(context, "Image hidden successfully", Toast.LENGTH_SHORT).show();
                            });
                        });
            }
        }).start();
    }
    private void hideImage(Bitmap finalBitmap, Context context) {
        // Define the hidden directory
        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics");
        if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

        // Generate the file name
        String fileName = ImgName.getText().toString().trim() + stringformat;
        File file = new File(hiddenDir, fileName);

        if (file.exists()) {
            Toast.makeText(context, "This name is already used", Toast.LENGTH_SHORT).show();
        } else {
            try (FileOutputStream out = new FileOutputStream(file)) {
                // Use the same compression format and quality
                finalBitmap.compress(compressFormat, imgQuality, out);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Notify the system that a new file has been created
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("HiddenStorage", "Scanned " + path + ":");
                            Log.i("HiddenStorage", "-> uri=" + uri);
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(context, "Image hidden successfully",
                                            Toast.LENGTH_SHORT).show());
                        }
                    });
        }
    }

    private void SaveImage(Bitmap finalBitmap,ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

        new Thread(() -> {
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/HelloPics");
            myDir.mkdirs();

            File file = new File(myDir, ImgName.getText().toString().trim() + stringformat);
            if (file.exists()) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar
                    Toast.makeText(Editor_main_Activity.this, "This name is already used", Toast.LENGTH_SHORT).show();
                });
            } else {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(compressFormat, imgQuality, out);
                    out.flush();
                    out.close();

                    MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                            (path, uri) -> {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                                runOnUiThread(() -> {
                                    progressBar.setVisibility(View.GONE); // Hide ProgressBar \nScanned " + path + ":\n" + uri
                                    myDialog.dismiss();
                                    Toast.makeText(Editor_main_Activity.this,
                                            "Image saved successfully",
                                            Toast.LENGTH_SHORT).show();
                                });
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        myDialog.dismiss();
                        Toast.makeText(Editor_main_Activity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }


    //save Image In folder.
    private void SaveImagee(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/HelloPics");
        myDir.mkdirs();

        File file = new File(myDir, ImgName.getText().toString().trim() + stringformat);
        if (file.exists()) {
            Toast.makeText(this, "this name is already used", Toast.LENGTH_SHORT).show();
        } else {


            //file.delete();
            try {

                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(compressFormat, imgQuality, out);

                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(
                                            Editor_main_Activity.this,
                                            "image saved success\n" + "Scanned " + path + ":\n" + uri,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("PNG")) {
            compressFormat = Bitmap.CompressFormat.PNG;
            stringformat = ".png";
        } else if (parent.getItemAtPosition(position).equals("JPEG")) {
            compressFormat = Bitmap.CompressFormat.JPEG;
            stringformat = ".jpg";
        } else {
            compressFormat = Bitmap.CompressFormat.WEBP;
            stringformat = ".webp";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class Tools_home_Adapter extends RecyclerView.Adapter<Tools_home_Adapter.MyViewHolder> {
        ArrayList<Editing_tools> editingtools;
        Context context;

        public Tools_home_Adapter(ArrayList<Editing_tools> editingtools, Context context) {
            this.editingtools = editingtools;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.editor_tools_home_item, parent, false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Glide.with(context)
                    .load(editingtools.get(position).getToolsImg_H())
                    .into(holder.imageView_Tool);
            holder.textView_Tool.setText(editingtools.get(position).getToolsName_H());
            holder.imageView_Tool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (editingtools.get(position).getToolsName_H()) {
                        case tools_name.TOOLS:
                            ShowRToolsDialog();
                            break;
                        case tools_name.ADJUST:
                            Intent AdjustImage = new Intent(Editor_main_Activity.this, AdjustImageActivity.class);
                            startActivity(AdjustImage);
                            finish();
                            break;

                        case tools_name.TEXT:
                            Intent TextOnImage = new Intent(Editor_main_Activity.this, TextOnImageActivity.class);
                            startActivity(TextOnImage);
                            finish();
                            break;
                        case tools_name.Add_Image:
                            Intent AddImage = new Intent(Editor_main_Activity.this, AddPhotoActivity.class);
                            startActivity(AddImage);
                            finish();
                            break;
                        case tools_name.BORDER:
                            Intent ImageBorder = new Intent(Editor_main_Activity.this, ImageBorderActivity.class);
                            startActivity(ImageBorder);
                            finish();
                            break;
                        case tools_name.RESIZE:
                            ShowImageResizeDialogue();
                            break;
                        case tools_name.DRAW:
                            Intent ImageDraw = new Intent(Editor_main_Activity.this, DrawingActivity.class);
                            startActivity(ImageDraw);
                            finish();
                            break;
                        case tools_name.GRADIENT:
                            Intent ImageGradient = new Intent(Editor_main_Activity.this, GradientActivity.class);
                            startActivity(ImageGradient);
                            finish();
                            break;
                        case tools_name.EFFECT:
                            Intent ImageEffects = new Intent(Editor_main_Activity.this, EffectsActivity.class);
                            startActivity(ImageEffects);
                            finish();
                            break;
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return editingtools.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView_Tool;
            TextView textView_Tool;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView_Tool = itemView.findViewById(R.id.imageView_Tool_Home);
                textView_Tool = itemView.findViewById(R.id.textView_Tool_Home);

            }
        }
    }


    public class DialiogToolsAdapter extends RecyclerView.Adapter<DialiogToolsAdapter.MyViewHolder> {
        Context context;
        ArrayList<Tools_Name> tools_names;
        BottomSheetDialog bottomSheetDialog;

        public DialiogToolsAdapter(Context context, ArrayList<Tools_Name> tools_names,BottomSheetDialog bottomSheetDialog) {
            this.context = context;
            this.tools_names = tools_names;
            this.bottomSheetDialog = bottomSheetDialog;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View View = layoutInflater.from(context).inflate(R.layout.dialog_h_item, parent, false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Glide.with(context)
                    .load(tools_names.get(position).getRToolsImg())
                    .into(holder.Rtoolsimg);
            holder.RToolsname.setText(tools_names.get(position).getRToolName());
            holder.Rtoolsimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (tools_names.get(position).getRToolName()) {
                        case tools_name.CROP -> {
                            Intent intentCropActivity = new Intent(Editor_main_Activity.this, CropActivity.class);
                            startActivity(intentCropActivity);
                            finish();
                        }
                        case tools_name.SELECT -> {
                            Intent intentSelectActivity = new Intent(Editor_main_Activity.this, Select_crop_Activity.class);
                            startActivity(intentSelectActivity);
                            finish();
                        }
                        case tools_name.FREE_CROP -> {
                            Intent intentFreeCropActivity = new Intent(Editor_main_Activity.this, FreeCropActivity.class);
                            startActivity(intentFreeCropActivity);
                            finish();
                        }
                        case tools_name.ROTATE -> {
                            Intent intentRotateActivity = new Intent(Editor_main_Activity.this, RotateImageActivity.class);
                            startActivity(intentRotateActivity);
                            finish();
                        }
                        case tools_name.PERSPECTIVE -> {
                            Intent intentScaleActivity = new Intent(Editor_main_Activity.this, ScaleActivity.class);
                            startActivity(intentScaleActivity);
                            finish();
                        }
                        case tools_name.STRETCH -> {
                            //dosent work
                            Intent intentSTRETCH = new Intent(Editor_main_Activity.this, StretchActivity.class);
                            startActivity(intentSTRETCH);
                            finish();
                        }
                        case tools_name.CLONE -> {
                            Intent intentClone = new Intent(Editor_main_Activity.this, CloneActivity.class);
                            startActivity(intentClone);
                            finish();
                        }
                        case tools_name.PERSPECTIVE_CROP -> {
                            Intent intentRespectiveCrop = new Intent(Editor_main_Activity.this, RespectiveCropActivity.class);
                            startActivity(intentRespectiveCrop);
                            finish();
                        }
                    }
                    bottomSheetDialog.cancel();
                }
            });
        }

        @Override
        public int getItemCount() {
            return tools_names.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageButton Rtoolsimg;
            TextView RToolsname;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                Rtoolsimg = itemView.findViewById(R.id.imageView_Tool_Real);
                RToolsname = itemView.findViewById(R.id.textView_Tool_Real);

            }
        }
    }



    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private ArrayList<Item> itemList;
        private Context context;

        public ItemAdapter(Context context, ArrayList<Item> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Create parent LinearLayout
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(20, 20, 20, 20);
            layout.setGravity(Gravity.CENTER);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create ImageView
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100, 100);
            imageParams.gravity = Gravity.CENTER; // Ensure it's centered
            imageView.setLayoutParams(imageParams);

            // Create TextView
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    200,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(20, 0, 0, 0);
            textParams.gravity = Gravity.CENTER; // Center text
            textView.setLayoutParams(textParams);
            textView.setTextSize(16);

            // Add views to layout
            layout.addView(imageView);
            layout.addView(textView);

            return new ViewHolder(layout, imageView, textView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.imageView.setImageBitmap(itemList.get(position).getImageResId());
            holder.textView.setText(itemList.get(position).getText());

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.setBitmap(itemList.get(position).getImageResId());
                    image.setOriginalBitmap(itemList.get(position).getImageResId());
                    mainImgZoomView.setImageBitmap(itemList.get(position).getImageResId());
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.setBitmap(itemList.get(position).getImageResId());
                    image.setOriginalBitmap(itemList.get(position).getImageResId());
                    mainImgZoomView.setImageBitmap(itemList.get(position).getImageResId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public ViewHolder(@NonNull View itemView, ImageView imageView, TextView textView) {
                super(itemView);
                this.imageView = imageView;
                this.textView = textView;
            }
        }
    }

    private void showDialog(Context context) {
        // Create RecyclerView programmatically
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Sample data
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(image.getOriginalBitmap(), "ORIGINAL"));

        // Set adapter
        recyclerView.setAdapter(new ItemAdapter(context, items));

        // Create AlertDialog programmatically
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an Option");

        // Create a container layout for the title and RecyclerView
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(20, 20, 20, 20);
        container.addView(recyclerView);

        builder.setView(container);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}