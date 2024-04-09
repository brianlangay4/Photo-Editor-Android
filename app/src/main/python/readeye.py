import cv2
import numpy as np

def fillHoles(mask):
    maskFloodfill = mask.copy()
    h, w = maskFloodfill.shape[:2]
    maskTemp = np.zeros((h+2, w+2), np.uint8)
    cv2.floodFill(maskFloodfill, maskTemp, (0, 0), 255)
    mask2 = cv2.bitwise_not(maskFloodfill)
    return mask2 | mask

def removeRedEyes(image_data):
    try:
        # Convert the byte array to a NumPy array
        nparr = np.frombuffer(image_data, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        imgOut = img.copy()

        # Load HAAR cascade for eye detection
        eyesCascade = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_eye.xml")

        # Detect eyes
        eyes = eyesCascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(100, 100))

        # For every detected eye
        for (x, y, w, h) in eyes:
            # Extract eye from the image
            eye = img[y:y+h, x:x+w]

            # Split eye image into 3 channels
            b = eye[:, :, 0]
            g = eye[:, :, 1]
            r = eye[:, :, 2]

            # Add the green and blue channels.
            bg = cv2.add(b, g)

            # Simple red eye detector.
            mask = (r > 150) &  (r > bg)

            # Convert the mask to uint8 format.
            mask = mask.astype(np.uint8) * 255

            # Clean mask -- Fill holes and dilate mask.
            mask = fillHoles(mask)
            mask = cv2.dilate(mask, None, anchor=(-1, -1), iterations=3, borderType=1, borderValue=1)

            # Calculate the mean channel by averaging the green and blue channels
            mean = bg / 2
            mask = mask.astype(bool)[:, :, np.newaxis]
            mean = mean[:, :, np.newaxis]

            # Copy the eye from the original image.
            eyeOut = eye.copy()

            # Copy the mean image to the output image.
            eyeOut = np.where(mask, mean, eyeOut)

            # Copy the fixed eye to the output image.
            imgOut[y:y+h, x:x+w, :] = eyeOut

        # Serialize the processed image data
        _, buffer = cv2.imencode('.jpg', imgOut)
        return buffer.tobytes()
    except Exception as e:
        print(f"Error processing image: {e}")
        return None
