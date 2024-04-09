import cv2
import numpy as np
import dlib
import os
import random

# Get the directory of the current script
current_dir = os.path.dirname(os.path.abspath(__file__))

# Construct the file path for shape_predictor_68_face_landmarks.dat
landmarks_file = os.path.join(current_dir, "shape_predictor_68_face_landmarks.dat")

# Check if the landmarks file exists
if os.path.exists(landmarks_file):
    # Initialize face detector and landmark detector with the dynamically constructed file path
    faceDetector = dlib.get_frontal_face_detector()
    landmarkDetector = dlib.shape_predictor(landmarks_file)
else:
    # Handle the case where the landmarks file does not exist
    raise FileNotFoundError("shape_predictor_68_face_landmarks.dat not found")

# Define lipstick colors
lipstick_colors = {"vamptastic_plum": (97, 45, 130),
                   "red_dahlia": (51, 30, 136),
                   "flamenco_red": (42, 31, 192),
                   "chery_red": (63, 45, 222),
                   "caramel_nude": (120, 131, 201),
                   "mango_tango": (103, 92, 223),
                   "neon_red": (79, 32, 223),
                   "electric_orchid": (139, 64, 243),
                   "forbbiden_fuchsia": (105, 39, 184),
                   "sweet_marsala": (93, 67, 164)}

def get_lips_mask(size, lips):
    # Find Convex hull of all points
    hullIndex = cv2.convexHull(np.array(lips), returnPoints=False)
    # Convert hull index to list of points
    hullInt = []
    for hIndex in hullIndex:
        hullInt.append(lips[hIndex[0]])
    # Create mask such that convex hull is white
    mask = np.zeros((size[0], size[1], 3), dtype=np.uint8)
    cv2.fillConvexPoly(mask, np.int32(hullInt), (255, 255, 255))
    return mask

def apply_color_to_mask(mask):
    # Get random lipstick color
    color_name, color = random.choice(list(lipstick_colors.items()))
    print("[INFO] Color Name: {}".format(color_name))
    b, g, r = cv2.split(mask)
    b = np.where(b > 0, color[0], 0).astype('uint8')
    g = np.where(g > 0, color[1], 0).astype('uint8')
    r = np.where(r > 0, color[2], 0).astype('uint8')
    return cv2.merge((b, g, r)), color_name

def apply_lipstick(image_data):
    # Convert image byte data to numpy array
    nparr = np.frombuffer(image_data, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # Calculate landmarks
    points = get_landmarks(img)

    if points is None:
        print("No faces detected")
        return image_data

    # Select points of lips and create "lips mask"
    lips = [points[x] for x in range(48, 68)]
    contours = [np.asarray(lips, dtype=np.int32)]
    (x, y, w, h) = cv2.boundingRect(contours[0])
    center = (int(x+w/2), int(y+h/2))
    mask = get_lips_mask(img.shape, lips)

    # Apply color to mask
    color_mask, color_name = apply_color_to_mask(mask)

    # Apply lipstick color to the lips area
    result = cv2.addWeighted(img, 1, color_mask, 0.2, 0)

    # Convert the modified image to byte array
    retval, buffer = cv2.imencode('.jpg', result)
    return buffer.tobytes()

def get_landmarks(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    rects = faceDetector(gray, 0)

    if len(rects) == 0:
        return None

    rect = rects[0]
    shape = landmarkDetector(gray, rect)
    points = np.zeros((68, 2), dtype=int)
    for i in range(0, 68):
        points[i] = (shape.part(i).x, shape.part(i).y)

    return points
