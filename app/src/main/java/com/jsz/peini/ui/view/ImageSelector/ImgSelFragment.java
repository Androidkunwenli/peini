package com.jsz.peini.ui.view.ImageSelector;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.ui.view.ImageSelector.adapter.FolderListAdapter;
import com.jsz.peini.ui.view.ImageSelector.adapter.ImageListAdapter;
import com.jsz.peini.ui.view.ImageSelector.adapter.PreviewAdapter;
import com.jsz.peini.ui.view.ImageSelector.bean.Folder;
import com.jsz.peini.ui.view.ImageSelector.bean.Image;
import com.jsz.peini.ui.view.ImageSelector.common.Callback;
import com.jsz.peini.ui.view.ImageSelector.common.Constant;
import com.jsz.peini.ui.view.ImageSelector.common.OnFolderChangeListener;
import com.jsz.peini.ui.view.ImageSelector.common.OnItemClickListener;
import com.jsz.peini.ui.view.ImageSelector.utils.FileUtils;
import com.jsz.peini.ui.view.ImageSelector.utils.LogUtils;
import com.jsz.peini.ui.view.ImageSelector.widget.CustomViewPager;
import com.jsz.peini.ui.view.ImageSelector.widget.DividerGridItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImgSelFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RecyclerView rvImageList;
    private Button btnAlbumSelected;
    private View rlBottom;
    private CustomViewPager viewPager;

    private ImgSelConfig config;
    private Callback callback;
    private List<Folder> folderList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();

    private ListPopupWindow folderPopupWindow;
    private ImageListAdapter imageListAdapter;
    private FolderListAdapter folderListAdapter;
    private PreviewAdapter previewAdapter;

    private boolean hasFolderGened = false;
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 5;

    private File tempFile;

    public static ImgSelFragment instance() {
        ImgSelFragment fragment = new ImgSelFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_sel, container, false);
        rvImageList = (RecyclerView) view.findViewById(R.id.rvImageList);
        btnAlbumSelected = (Button) view.findViewById(R.id.btnAlbumSelected);
        btnAlbumSelected.setOnClickListener(this);
        rlBottom = view.findViewById(R.id.rlBottom);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        config = Constant.config;
        try {
            callback = (Callback) getActivity();
        } catch (Exception e) {

        }

        btnAlbumSelected.setText(config.allImagesText);

        rvImageList.setLayoutManager(new GridLayoutManager(rvImageList.getContext(), 4));
        rvImageList.addItemDecoration(new DividerGridItemDecoration(rvImageList.getContext()));
        if (config.needCamera)
            imageList.add(new Image());

        imageListAdapter = new ImageListAdapter(getActivity(), imageList, config);
        imageListAdapter.setShowCamera(config.needCamera);
        imageListAdapter.setMutiSelect(config.multiSelect);
        rvImageList.setAdapter(imageListAdapter);
        imageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public int onCheckedClick(int position, Image image) {
                return checkedImage(position, image);
            }

            @Override
            public void onImageClick(int position, Image image) {
                if (config.needCamera && position == 0) {
                    showCameraAction();
                } else {
                    if (config.multiSelect) {
                        viewPager.setAdapter((previewAdapter = new PreviewAdapter(getActivity(), imageList, config)));
                        previewAdapter.setListener(new OnItemClickListener() {
                            @Override
                            public int onCheckedClick(int position, Image image) {
                                return checkedImage(position, image);
                            }

                            @Override
                            public void onImageClick(int position, Image image) {
                                hidePreview();
                            }
                        });
                        if (config.needCamera) {
                            callback.onPreviewChanged(position, imageList.size() - 1, true);
                        } else {
                            callback.onPreviewChanged(position + 1, imageList.size(), true);
                        }
                        viewPager.setCurrentItem(config.needCamera ? position - 1 : position);
                        viewPager.setVisibility(View.VISIBLE);
                    } else {
                        if (callback != null) {
                            callback.onSingleImageSelected(image.path);
                        }
                    }
                }
            }
        });

        folderListAdapter = new FolderListAdapter(getActivity(), folderList, config);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private int checkedImage(int position, Image image) {
        if (image != null) {
            if (Constant.imageList.contains(image.path)) {
                Constant.imageList.remove(image.path);
                if (callback != null) {
                    callback.onImageUnselected(image.path);
                }
            } else {
                if (config.maxNum <= (Constant.imageList.size() + config.list)) {
                    Toast.makeText(getActivity(), String.format(getString(R.string.maxnum), config.maxNum), Toast.LENGTH_SHORT).show();
                    return 0;
                }
                Constant.imageList.add(image.path);
                if (callback != null) {
                    callback.onImageSelected(image.path);
                }
            }
            return 1;
        }
        return 0;
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        int[] imageWidthHeight = getImageWidthHeight(path);
                        int width = imageWidthHeight[0];
                        int height = imageWidthHeight[1];
                        long length = new File(path).length();
                        if (!image.path.endsWith("gif") && width > 0 && height > 0 && length > 0)
                            tempImageList.add(image);
                        if (!hasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!folderList.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                folderList.add(folder);
                            } else {
                                Folder f = folderList.get(folderList.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);


                    imageListAdapter.notifyDataSetChanged();

                    if (Constant.imageList != null && Constant.imageList.size() > 0) {
                        //imageListAdapter.setDefaultSelected(Constant.imageList);
                    }

                    folderListAdapter.notifyDataSetChanged();

                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(getActivity());
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
        folderPopupWindow.setAdapter(folderListAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(rlBottom);
        folderPopupWindow.setModal(true);
        folderListAdapter.setOnFloderChangeListener(new OnFolderChangeListener() {
            @Override
            public void onChange(int position, Folder folder) {
                folderPopupWindow.dismiss();
                if (position == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    btnAlbumSelected.setText(config.allImagesText);
                } else {
                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(folder.images);
                    imageListAdapter.notifyDataSetChanged();

                    btnAlbumSelected.setText(folder.name);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAlbumSelected.getId()) {
            if (folderPopupWindow == null) {
                WindowManager wm = getActivity().getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                createPopupFolderList(width / 3 * 2, width / 3 * 2);
            }

            if (folderPopupWindow.isShowing()) {
                folderPopupWindow.dismiss();
            } else {
                folderPopupWindow.show();
                if (folderPopupWindow.getListView() != null) {
                    folderPopupWindow.getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.bottom_bg)));
                }
                int index = folderListAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopupWindow.getListView().setSelection(index);
            }
        }
    }

    private void showCameraAction() {
        if (config.maxNum <= Constant.imageList.size()) {
            Toast.makeText(getActivity(), String.format(getString(R.string.maxnum), config.maxNum), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            tempFile = new File(FileUtils.createRootPath(getActivity()) + "/" + System.currentTimeMillis() + ".jpg");
            LogUtils.e(tempFile.getAbsolutePath());
            FileUtils.createFile(tempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), getString(R.string.open_camera_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    if (callback != null) {
                        callback.onCameraShot(tempFile);
                    }
                }
            } else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (config.needCamera) {
            callback.onPreviewChanged(position + 1, imageList.size() - 1, true);
        } else {
            callback.onPreviewChanged(position + 1, imageList.size(), true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean hidePreview() {
        if (viewPager.getVisibility() == View.VISIBLE) {
            viewPager.setVisibility(View.GONE);
            callback.onPreviewChanged(0, 0, false);
            imageListAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }
}
