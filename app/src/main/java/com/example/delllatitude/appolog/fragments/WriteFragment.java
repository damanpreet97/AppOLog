package com.example.delllatitude.appolog.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.DetailedBlogActivity;
import com.example.delllatitude.appolog.activities.LoginActivity;
import com.example.delllatitude.appolog.models.Blog;
import com.example.delllatitude.appolog.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import jp.wasabeef.richeditor.RichEditor;

public class WriteFragment extends Fragment {
    ActionBar actionBar;
    Button btnPost;
    LinearLayout addImageLayout;
    RelativeLayout showImageLayout;
    ImageView imvTitleImage, editImage;
    FloatingActionButton addTitleImage;
    EditText title;
    RichEditor editor;
    String blogTitle, blogContent, blogAuthorName, blogAuthorImage, blogMainImage, blogAuthorID, blogID;
    Uri selectedImage;
    Long blogTime;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currUser = firebaseAuth.getCurrentUser();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    public static final int GET_FROM_GALLERY = 3;

//    ScrollView parentScrollView, childScrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currUser == null) {
            launchLoginActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_write, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnPost = view.findViewById(R.id.btnWritePost);
        addTitleImage = view.findViewById(R.id.fabImage);
        editImage = view.findViewById(R.id.imgEditBtn);
        showImageLayout = view.findViewById(R.id.layoutShowImage);
        addImageLayout = view.findViewById(R.id.llAddImage);
        imvTitleImage = view.findViewById(R.id.imvTitleImage);
        title = view.findViewById(R.id.etWriteTitle);
        editor = view.findViewById(R.id.editor);

//        Ensure that you disable debugging for your app if using WebView to display paid for content
//        or if using JavaScript interfaces, since debugging allows users to inject scripts and extract
//        content using Chrome DevTools.
        editor.setWebContentsDebuggingEnabled(false);

        initializeEditorTools(view);
        addTitleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPost.setPressed(true);
//                btnPost.setEnabled(false);
                if (currUser != null && title.getText()!=null && !title.getText().toString().equals("") && editor.getHtml()!=null) {
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    if(selectedImage!=null) {
                        uploadImageToFbStorage(selectedImage);
                    }else {
                    Blog createdBlog = extractDetails();
                    pushToRealtimeDatabase(createdBlog);
                    showPostSuccessfulMessage(createdBlog);
                    }

                } else {
                    if(currUser == null)
                    Toast.makeText(getContext(), "You will need to Login before Posting", Toast.LENGTH_LONG).show();
                    else if(title.getText()==null || title.getText().toString().equals(""))
                        Toast.makeText(getContext(), "Blog without a Title cannot be posted", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Blog without Content cannot be posted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initializeEditorTools(View view) {
        view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.undo();
            }
        });

        view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.redo();
            }
        });

        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setBold();
            }
        });

        view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setItalic();
            }
        });

        view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setSubscript();
            }
        });

        view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setSuperscript();
            }
        });

        view.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setStrikeThrough();
            }
        });

        view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setUnderline();
            }
        });

        view.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(1);
            }
        });

        view.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(2);
            }
        });

        view.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(3);
            }
        });

        view.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(4);
            }
        });

        view.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(5);
            }
        });

        view.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setHeading(6);
            }
        });

        view.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setIndent();
            }
        });

        view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setOutdent();
            }
        });

        view.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setAlignLeft();
            }
        });

        view.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setAlignCenter();
            }
        });

        view.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setBullets();
            }
        });
//
//        view.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                editor.insertLink("https://github.com/wasabeef", "wasabeef");
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Write Blog");
        }
        getActivity().findViewById(R.id.tvAppTitle).setVisibility(View.GONE);
    }

    //This method pushes the blog object into the firebase's realtime database.
    // It is called from within the uploadImageToFbStorage(Uri uri) method.
    // blogId parameter is also set here.
    private void pushToRealtimeDatabase(Blog createdBlog) {
        DatabaseReference rootReference = firebaseDatabase.getReference();
        DatabaseReference blogsReference = rootReference.child("Blogs");
        DatabaseReference currBlogReference = blogsReference.push();
        DatabaseReference currUserReference = rootReference.child("Users").child(currUser.getUid());
        final DatabaseReference currUserDetailsReference = currUserReference.child("Details");
        DatabaseReference currUserBlogsReference = currUserReference.child("BlogList");

        //sets the key for blogList item same as its value i.e. the blogID
        currUserBlogsReference.child(currBlogReference.getKey()).setValue(currBlogReference.getKey());

        createdBlog.setBlogID(currBlogReference.getKey());
        currBlogReference.setValue(createdBlog);

        //This section updates the value for "blogsPosted" attribute of the user in the database
        final int[] currUserBlogsPosted = {0};
        currUserDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currUserObj = dataSnapshot.getValue(User.class);
                if(currUserObj!=null) {
                    currUserBlogsPosted[0] = currUserObj.getBlogsPosted() + 1;
                    currUserObj.setBlogsPosted(currUserBlogsPosted[0]);
                    //set the updated value in database
                    currUserDetailsReference.child("blogsPosted").setValue(currUserBlogsPosted[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1234) {
            currUser = firebaseAuth.getCurrentUser();
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "Login Succesfful", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "You need to Login First to make a Post", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
//            Bitmap bitmap = null;

//                blogMainImageUri = selectedImage;
            showImageEdit();

                Picasso.get().load(selectedImage.toString()).fit().into(imvTitleImage);
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);


//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showImageEdit() {
        addImageLayout.setVisibility(View.INVISIBLE);
        showImageLayout.setVisibility(View.VISIBLE);
    }

    //This method uploads the image selected by the user from his local storage to firebase storage and then
    // pushes the blog onto the database
    private void uploadImageToFbStorage(Uri filePath) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference imageReference = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = imageReference.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                    imageReference.getDownloadUrl();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

//This section generates a download link for the image uploaded
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        blogMainImage = downloadUri.toString();
                        Blog createdBlog = extractDetails();
                        pushToRealtimeDatabase(createdBlog);
                        showPostSuccessfulMessage(createdBlog);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }

    }

    private void showPostSuccessfulMessage(final Blog createdBlog) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("Post Successful!")
                .setMessage("Your blog was successfully posted :)")
                .setCancelable(false)
                .setPositiveButton("View Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getContext(), DetailedBlogActivity.class);
                        i.putExtra("blog", createdBlog);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetData();
                        dialog.dismiss();
                    }
                }).create();

        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }

    private void resetData() {
        title.setText("");
        showAddImage();
        editor.setHtml("");
//        imvTitleImage.setImageResource(R.drawable.download1);
    }

    private void showAddImage() {
        addImageLayout.setVisibility(View.VISIBLE);
        showImageLayout.setVisibility(View.GONE);
    }


    private void launchLoginActivity() {
        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(loginIntent, 1234);
    }

    // This method extracts the details inputed by the user in the write fragment of he app while creating the blog
    // blogId is set to null here always. Its correct value is set at the time of posting the blog in pushTorealtimeDatabase method
    private Blog extractDetails() {
        blogTitle = title.getText().toString();
        blogContent = editor.getHtml();
        blogTime = System.currentTimeMillis();
        blogAuthorName = currUser.getDisplayName();
        blogAuthorID = currUser.getUid();

        if (currUser.getPhotoUrl() != null) {
            blogAuthorImage = currUser.getPhotoUrl().toString();
        }

        Blog blog = new Blog(blogTime, blogTitle, blogAuthorName, blogAuthorImage, blogMainImage, 0, blogContent, blogAuthorID, blogID);
        return blog;
    }

    @Override
    public void onStop() {
        actionBar.setDisplayShowTitleEnabled(false);
        getActivity().findViewById(R.id.tvAppTitle).setVisibility(View.VISIBLE);
//        getActivity().findViewById(R.id.search_image_view).setVisibility(View.VISIBLE);
        super.onStop();
    }

}