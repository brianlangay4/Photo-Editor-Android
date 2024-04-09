### Photo Editor Application Overview

The  Android app demonstrates sophisticated photo editing capabilities by combining intuitive UI elements with powerful backend image processing. Users can select an image from their device, apply various edits including brightness and intensity adjustments, lipstick application, and red-eye removal, and then view the edited image directly within the app.

### Follow the Article on How we archive this image manipulation techiniques with OpenCV in Python available in my page Kaggle

<a href="https://www.kaggle.com/datasets/brianlangay/red-eye-removal" target="_blank">🔗 click to view article 👉</a>

### Preview 
![photoEditor 0 0 1](https://github.com/brianlangay4/Photo-Editor-Android/assets/67788456/211ee2e4-4422-47cb-8192-3fa2b9d40f54)
#

### Key Features

- **Image Selection:** Enables users to pick an image from their device for editing.
- **Brightness Adjustment:** Allows for the modification of the image's brightness level.
- **Intensity Adjustment:** Offers the capability to alter the image's intensity for more vivid visuals.
- **Lipstick Application:** Users can apply a lipstick effect to images.
- **Red-eye Removal:** Provides a feature to automatically remove red eyes from photos, enhancing the quality of portraits.

### Main Components

- **ImageView (`imageView`):** Displays the selected image and the result of the applied edits.
- **SeekBar (`intensity`, `brightness`):** UI sliders for adjusting the intensity and brightness levels of the image.
- **Buttons (`apply`, `magic`, `eye`, `lips`):** Triggers for applying specific edits or opening the image selector.
- **TextViews (`textView`, `textView2`):** Displays the current values of adjustments for user reference.

### Initialization and Setup

- **Python Initialization:** Essential for backend image processing, Python scripts are utilized for executing photo editing tasks. Initialization occurs at app launch.
- **UI Element Setup:** Initializes UI components and sets visibility states and event listeners for interactive features.

### Image Editing Process

- **Brightness and Intensity Adjustments:** Utilizes SeekBars for user input on desired levels, with real-time updates displayed on the image.
- **Lipstick and Red-eye Removal:** Specialized Python scripts are called upon the selection of these options, demonstrating the app's ability to target specific image enhancements.

### User Interaction

- **Selective UI Visibility:** Adjusts the visibility of UI components based on the editing option selected by the user, ensuring a focused and uncluttered interface.
- **Interactive Image Selection:** A user-friendly method allows for the selection and loading of images from the device's storage, readying them for editing.

### Integration of Python Scripts for Image Processing

- **Backend Processing:** Python scripts are called with parameters like image data and desired adjustment levels. These scripts perform the heavy lifting for image manipulation, returning the edited image data for display.
- **Dynamic Editing:** Shows the application's flexibility in applying multiple edits, where users can adjust brightness, intensity, apply lipstick, or remove red eyes as needed, with instant visual feedback.

### Error Handling and Resource Management

- **Graceful Error Handling:** Includes checks for potential issues during image processing, notifying the user if an edit cannot be applied.
- **Efficient Resource Use:** Demonstrates careful management of memory and resources, particularly in handling image data and cleaning up after Python script executions.
