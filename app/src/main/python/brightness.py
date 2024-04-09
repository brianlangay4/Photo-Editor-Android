from PIL import ImageEnhance, Image
import io

def increase_brightness(image_data, percentage):
    try:
        # Open the image from byte data
        img = Image.open(io.BytesIO(image_data))
        
        # Create an ImageEnhance object
        enhancer = ImageEnhance.Brightness(img)
        
        # Adjust brightness by percentage
        modified_img = enhancer.enhance(1 + percentage / 100)
        
        # Serialize the new image data to byte array
        with io.BytesIO() as output:
            modified_img.save(output, format='PNG')
            modified_image_data = output.getvalue()
        
        # Return the modified image data
        return modified_image_data
    except Exception as e:
        print(f"Error processing image: {e}")
        return None
