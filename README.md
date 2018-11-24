
# Background
Random-dot-stereogram technique was first published by Dr. Julesz<sup>[10]</sup> in 1970s.  Since then, Random dot stereogram has experienced surges of popularity.  The algorithm is available from GPU Gems - Chapter 41 Real-Time Stereogram <sup>[0]</sup> and I have already tested it in javascript (available in appendix)<sup>[9]</sup>.

# Introduction
This repository contains proposal and java source code to satisfy Udacity Android nano-degree capstone project for calendar year 2018.  All code is written with Android Studio 3.1 and tested on Samsung S9 and emulators:[Nexus5, Tablet Nexus9] with OS8.0 Oreo, API 28.

# CapStoneStage1
Udacity Android nano-degree CapStone project stage 1: proposal
https://github.com/yeuchi/CapStoneStage1/blob/master/Proposal/Capstone_Stage1.pdf

# CapStoneStage2
Udacity Android nano-degree CapStone project stage 2: code implementation

### Application Main and Configuration 
Initial activities are main and configuration.

<img src="https://user-images.githubusercontent.com/1282659/48218830-666b5880-e350-11e8-8e22-498e89913697.png" width="200"><img src="https://user-images.githubusercontent.com/1282659/48297077-c4d62b00-e465-11e8-8cd1-89e09d7513a6.png" width="200"><img src="https://user-images.githubusercontent.com/1282659/48202400-5c366380-e32a-11e8-8201-ccd31b91d53a.png" width="200"><img src="https://user-images.githubusercontent.com/1282659/48202404-5e98bd80-e32a-11e8-8616-b56c471d3ef6.png" width="200">

### Fragments: create/share message
Random dot generation and sharing of the message is accomplished in the tab activity with fragments (Send, Text, Shape, Preview). User may encode a random dot message via shapes and/or text SVGs.  There is also header and footer text message that accompany the Random-dot-images.

<img src="https://user-images.githubusercontent.com/1282659/48087463-fd0b0e80-e1c4-11e8-897a-2bff6addf505.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48087464-fd0b0e80-e1c4-11e8-95bd-044291054451.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48087465-fd0b0e80-e1c4-11e8-93c4-6c5ef67d995a.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48087466-fd0b0e80-e1c4-11e8-835e-1281efd0b9b0.jpg" width="200">

### Share options
Below demonstrates sharing via email, Google Drive, Facebook and SMS.

<img src="https://user-images.githubusercontent.com/1282659/47918808-995aab80-de7b-11e8-80b8-31e16d942fb7.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/47888046-5adbd700-de10-11e8-87ae-319139225d32.png" width="200">

### Save on Google Drive
<img src="https://user-images.githubusercontent.com/1282659/47888044-5a434080-de10-11e8-8040-f5a2d0ce4507.png" width="200">.    <img src="https://user-images.githubusercontent.com/1282659/47919396-4124a900-de7d-11e8-8d7c-771e37a8f9cb.png" width="500">

### Share via Email
<img src="https://user-images.githubusercontent.com/1282659/47888043-5a434080-de10-11e8-8a5e-2235c06fd643.png" width="200">      <img src="https://user-images.githubusercontent.com/1282659/47918935-053d1400-de7c-11e8-938d-e131e3410d82.png" width="500">

### Share on Facebook
<img src="https://user-images.githubusercontent.com/1282659/47918807-995aab80-de7b-11e8-935f-7c880573828f.jpg" width="200">.   <img src="https://user-images.githubusercontent.com/1282659/47918939-079f6e00-de7c-11e8-9347-5098afb044aa.png" width="500">

### Viewer
Stereo pair can be difficult to detect sometimes, a debug mode has been added to colorize only the stereo elements; background is left black and white (left).  Note: viewer supports only landscape mode on phones.  I recommend the use of a Virtual Reality viewer for optimal viewing of stereo-pair.  Else, the traditional cross-eye viewing technique works. 
<img src="https://user-images.githubusercontent.com/1282659/48165971-dfb16f80-e2ac-11e8-8443-744fbf3e2f08.png" width="400"><img src="https://user-images.githubusercontent.com/1282659/48166591-a37f0e80-e2ae-11e8-86cb-1ee5a50875a7.png" width="400">

### Espresso tests
User interface tests are available at link belows.
https://github.com/yeuchi/CapStoneStage1/tree/master/app/src/androidTest/java/com/example/ctyeung/capstonestage1

### Localization (Arabic) 
This application currently supports languages, English and Arabic.  Implementating Arabic<sup>[6]</sup> present the feature of right->left alignment and additional SVG font glyphs.

<img src="https://user-images.githubusercontent.com/1282659/48635168-0480a380-e98d-11e8-8ebc-62d79745b76f.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48635166-0480a380-e98d-11e8-80b6-2dd6502450c7.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48635165-0480a380-e98d-11e8-967a-40e0e998a749.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48635169-0480a380-e98d-11e8-8f98-063a7360bc6d.jpg" width="200">
<img src="https://user-images.githubusercontent.com/1282659/48075859-afcd7380-e1a9-11e8-9a84-5edec4f22582.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48075863-b1973700-e1a9-11e8-90ce-1a97e14f0846.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48098646-00ad8e00-e1e3-11e8-8c9d-ab2d49721ff5.jpg" width="200"><img src="https://user-images.githubusercontent.com/1282659/48083703-7bfb4980-e1bb-11e8-8c6e-8c467b6ef986.jpg" width="200">

### Layout: Phone landscape 
<img width="400" src="https://user-images.githubusercontent.com/1282659/48218239-07591400-e34f-11e8-9f88-aeaae9d00428.png"> <img width="400" src="https://user-images.githubusercontent.com/1282659/48218231-06c07d80-e34f-11e8-93d1-e5a737b27405.png">

<img width="400" src="https://user-images.githubusercontent.com/1282659/48218232-06c07d80-e34f-11e8-8908-dd11cdc3ee1c.png"><img width="400" src="https://user-images.githubusercontent.com/1282659/48218233-06c07d80-e34f-11e8-98f7-df5990508b00.png">

<img width="400" src="https://user-images.githubusercontent.com/1282659/48218234-07591400-e34f-11e8-9d7c-7ba96ee16570.png"><img width="400" src="https://user-images.githubusercontent.com/1282659/48218236-07591400-e34f-11e8-8c77-de068da0e520.png">

<img width="400" src="https://user-images.githubusercontent.com/1282659/48218237-07591400-e34f-11e8-95ba-1a1b398e94a2.png"> <img width="400" src="https://user-images.githubusercontent.com/1282659/48218238-07591400-e34f-11e8-9964-c09b365bde0f.png">

<img width="400" src="https://user-images.githubusercontent.com/1282659/48218221-02946000-e34f-11e8-948a-41d704e3df57.png">

### Layout: Tablet 
<img width="350" src="https://user-images.githubusercontent.com/1282659/48220629-73d71180-e355-11e8-8832-01dafb97da18.png"><img width="350" src="https://user-images.githubusercontent.com/1282659/48220625-733e7b00-e355-11e8-8814-36069b9a69ca.png">
<img width="350" src="https://user-images.githubusercontent.com/1282659/48220623-733e7b00-e355-11e8-8bbb-0e2aed4b56e6.png"><img width="350" src="https://user-images.githubusercontent.com/1282659/48220624-733e7b00-e355-11e8-8b18-830ce031d8f1.png">
<img width="350" src="https://user-images.githubusercontent.com/1282659/48220626-73d71180-e355-11e8-8c5b-5559c8f608fe.png"><img width="350" src="https://user-images.githubusercontent.com/1282659/48220627-73d71180-e355-11e8-84c4-5034aaff6e26.png">
<img width="350" src="https://user-images.githubusercontent.com/1282659/48220628-73d71180-e355-11e8-9a07-86d70b9ad400.png"><img width="350" src="https://user-images.githubusercontent.com/1282659/48220630-73d71180-e355-11e8-9ac6-803a000551d3.png">
<img width="500" src="https://user-images.githubusercontent.com/1282659/48234522-8e25e500-e37f-11e8-8584-8704695cde19.png">

### Accessibility: Oral configuration

Numeric configuration editing is available through the use of Google speechRecognizer<sup>[12]</sup> feature.
Start by a click on floating-action-button, accessibility, and voice one of below options follow by a numeric value.  Please ignore the Google message on screen, "Select an application".

<img src="https://user-images.githubusercontent.com/1282659/48280981-dd6d2380-e41a-11e8-8a19-6340e7f77ba7.jpg" width="200"><img width="200" src="https://user-images.githubusercontent.com/1282659/48280306-d9d89d00-e418-11e8-8fe8-3174c168ee7d.jpg">

Limited voice configurations are available for the following.
1. Parallax distance (1 - 40)
2. Image height (200 - 800)
3. Interlace width (50 - 600)
4. Border offset (50 - 200)

Below dialog, 1st line contains the explict dictation result.  2nd line contains the parsed command and 3rd line is the interpreted numeric value.

<img width="200" src="https://user-images.githubusercontent.com/1282659/48280305-d9d89d00-e418-11e8-9b94-29ed91fcb090.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48280309-da713380-e418-11e8-9886-24b92ced8d9e.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48280310-da713380-e418-11e8-99fa-fd076831f16b.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48280311-da713380-e418-11e8-8426-6550f134025d.jpg">

Failure(s) may occur when dictation characters cannot be matched or numeric value fail to parse.  Cancel and try again.
<img width="200" src="https://user-images.githubusercontent.com/1282659/48280307-d9d89d00-e418-11e8-962c-693c223273ee.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48280308-da713380-e418-11e8-8597-06cd2744c10e.jpg">

### Widget - image listing

From Widget, a list of send images is presented, below-left.  Each tuple contains id, subject and time stamp.  When user touch select on the list, viewer is launched with the specified selection, below-right.

<img width="200" src="https://user-images.githubusercontent.com/1282659/48574354-95e00f00-e8d4-11e8-8a43-67bf9c70e715.jpg"><img width="400" src="https://user-images.githubusercontent.com/1282659/48595626-25a8ac00-e91b-11e8-8b27-dcfb8cabc475.jpg">

### Firebase - authentication

Firebase authentication has been added.
Create your own account and replace with your own google-services.json
<img width="200" src="https://user-images.githubusercontent.com/1282659/48655166-5ea75600-e9d9-11e8-9a3c-082f339a21ce.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48655168-5ea75600-e9d9-11e8-8d15-e0befdbad3c9.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48655167-5ea75600-e9d9-11e8-829a-0031eaeaf537.jpg"><img width="200" src="https://user-images.githubusercontent.com/1282659/48655169-5ea75600-e9d9-11e8-8cd4-3e50fc1275e2.jpg">

https://firebase.google.com/docs/auth/android/firebaseui
<img width="600" src="https://user-images.githubusercontent.com/1282659/48655098-b09bac00-e9d8-11e8-8b27-bb91ad2a99f8.png">
<img width="600" src="https://user-images.githubusercontent.com/1282659/48655099-b2fe0600-e9d8-11e8-886b-a388a9058758.png">


# Original project instruction

### Project Overview
In the Capstone project, you will build an app of your own design in two stages. In Stage 1, you will design and plan the app, using a template that we provide in the "Instructions" node.

Creating and building your own app idea can be both exciting and daunting; ultimately, we want the experience to be rewarding. You'll apply a wealth of different concepts and components that you've learned across the Nanodegree to bring you own app idea to life.

To keep the process from becoming overwhelming (or simply chaotic), you will design and plan your app, and receive feedback, before you start building. This will help prevent and mitigate pain points you might run into along the way, and also replicates the process of professional Android Developers.

### Why this Project?
To become a proficient Android Developer, you need to design apps and make plans for how to implement them. This will involve choices such as how you will store data, how you will display data to the user, and what functionality to include in the app.

### What Will I Learn?
Through this project, you'll demonstrate the ability to communicate an app idea formally, using:

### An app description
UI flow mocks, similar to what you've seen in other Nanodegree projects, like the Popular Movies overview
A list of required tasks that you will complete to build the app, in order
The Capstone project will give you the experience you need to own the full development of an app. This first stage replicates the design and planning experience that proficient Android Developers are expected to demonstrate.

## Instruction

How Will I Complete this Project?

#### Supporting Courses
You will use the skills you learned in all of the previous core curriculum to complete this two-stage Capstone Project.

#### App Ideas
If you don't have an app idea of your own, feel free to choose one of these:

- Teleprompter app (we're always on the look-out for a good teleprompter app!)
- Fitness app
- Podcast app
- Travel app
- Reddit app 
https://docs.google.com/document/d/106i--s7OS-aiP8Kr1-e9ysqs0AgXkV9_Prg0zEK82Dw/pub?embedded=true

#### Required Tasks
Review the requirements for the app in this rubric 
https://review.udacity.com/#!/rubrics/64/view

Make a copy of this template 
https://docs.google.com/document/d/1gKP6RxykeekNk5bYxXIKjEitKDPdxpRyIaa9t50bLSA/edit

Rename the copy: "Capstone_Stage1"
- Fill out each section:
- App Description
- UI Mocks
- Key Considerations
- Next Steps: Required Tasks

Download the completed document as a PDF, and save it as: "Capstone_Stage1.pdf"
Submit the PDF document in a zip file or from a GitHub repo through the project submission portal.

## References

0. GPU Gems - Chapter 41. Real-Time Stereogram
http://developer.download.nvidia.com/books/HTML/gpugems/gpugems_ch41.html

1. Stack overflow, "Send Image in message body of email android"
https://stackoverflow.com/questions/32344927/send-image-in-message-body-of-email-android

2. Stack overflow, "FileProvider - IllegalArgumentException: Failed to find configured root"
https://stackoverflow.com/questions/42516126/fileprovider-illegalargumentexception-failed-to-find-configured-root

3. Stack overflow, "Converting a view to Bitmap without displaying it in Android?"
https://stackoverflow.com/questions/2801116/converting-a-view-to-bitmap-without-displaying-it-in-android

4. Stack overflow, "What is the list of supported languages/locales on Android?"
https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android

5. Fontspace SVG fonts for letter a -> k
http://www.fontspace.com/gluk/broshk

6. CreativeMarket, Origami - Colored Arabic font by Mostafa El Abasiry
https://creativemarket.com/mabasiry/2517032-Origami-Colored-Arabic-Font

7. Convertio: TTF to SVG converter 
https://convertio.co/ttf-svg/

8. Saving and Reading Bitmaps/Images from Internal memory in Android
https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android

9. My Random-dot-stereogram implementation
http://www.ctyeung.com/wordpress/?p=630

10. Julesz, Béla (1971). Foundations of Cyclopean Perception. Chicago: The University of Chicago Press. ISBN 0-226-41527-9.

11. Material Design Icons
https://materialdesignicons.com

12. Google speech recognizer
https://developer.android.com/reference/android/speech/SpeechRecognizer.html?hl=es

13. My speech recognizer exercise, dictation
https://github.com/yeuchi/Dictation

