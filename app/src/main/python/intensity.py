from PIL import Image
import io

def increase_intensity(image_data, percentage):
    try:
        # Open the image from byte data
        img = Image.open(io.BytesIO(image_data))
        
        # Get image size
        width, height = img.size
        
        # Create a new blank image with the same size and mode
        new_img = Image.new(img.mode, (width, height))
        
        # Increase intensity
        for x in range(width):
            for y in range(height):
                # Get pixel value
                pixel = img.getpixel((x, y))
                # Increase intensity by percentage
                new_pixel = tuple(int(min(255, max(0, component * (1 + percentage / 100)))) for component in pixel)
                # Set pixel value in the new image
                new_img.putpixel((x, y), new_pixel)
        
        # Serialize the new image data to byte array
        with io.BytesIO() as output:
            new_img.save(output, format='PNG')
            new_image_data = output.getvalue()
        
        # Return the modified image data
        return new_image_data
    except Exception as e:
        print(f"Error processing image: {e}")
        return None
