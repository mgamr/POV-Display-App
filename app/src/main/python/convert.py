from PIL import Image, ImageDraw, ImageStat, ImageColor
import time
import os

def convert(filename):
    # Start timer
    start_time = time.time()

    # Open image
    img = Image.open(filename)

    # Parameters
    deg = 3  # resolution
    pixels = 36  # Num of leds
    offset = 0  # degree offset
    width = 5  # pixel width
    offsetWidth = 6  # offset between pixels

    # Calculations
    sections = 360 / deg

    # Initialize 2D array
    array_2d = [["" for _ in range(pixels)] for _ in range(int(sections))]

    # Calculate max size for resizing the image
    max_size = (pixels * 2) * (offsetWidth)
    print("Max size:", max_size)
    print("Original image size:", img.size)

    # Resize image
    img = img.resize((max_size, max_size), 0)
    print("Resized image size:", img.size)

    # Create result image and mask
    resImg = Image.new('RGBA', img.size, (0, 0, 0, 0))
    drawRes = ImageDraw.Draw(resImg)

    inMask = Image.new('1', img.size, 0)
    inDraw = ImageDraw.Draw(inMask)

    # Calculate half dimensions
    halfWidth = img.width / 2
    halfHeight = img.height / 2

    # Iterate over each pixel
    for i in range(1, pixels + 1):
        curOffset = i * offsetWidth
        xyCor = (halfWidth - curOffset, halfHeight - curOffset), (halfWidth + curOffset, halfHeight + curOffset)

        for j in range(int(sections)):
            start_deg = j * deg
            end_deg = (j + 1) * deg - offset

            # Draw arcs in the mask
            inDraw.rectangle(xyCor, 0)
            inDraw.arc(xyCor, start_deg, end_deg, 1, width)

            # Calculate average color
            stat = ImageStat.Stat(img, mask=inMask)
            average_color = (0, 0, 0)
            if sum(stat.count) >= 1:
                average_color = tuple(int(channel) for channel in stat.mean)
                drawRes.arc(xyCor, start_deg, end_deg, average_color, width)

            # Convert average color to hex
            av_color = (average_color[1], average_color[0], average_color[2])
            hex_color = '0x00{:02X}{:02X}{:02X}'.format(*av_color)
            array_2d[j][i - 1] = hex_color

    # Show images
    img.show()
    resImg.show()

    # File paths
    base, ext = os.path.splitext(filename)
    h_filename = f"{base}.h"
    json_filename = f"{base}.json"

    # Write to .h file
    with open(h_filename, "w") as h_file:
        h_file.write("const uint32_t fontHEX[][36]={\n")
        for i, line in enumerate(array_2d):
            h_file.write("{")
            for j, col in enumerate(line):
                h_file.write(str(int(col, 16)))
                if j != len(line) - 1:
                    h_file.write(",")
            h_file.write("},\n" if i != len(array_2d) - 1 else "}")
        h_file.write("\n};\n")

    # Write to .json file
    with open(json_filename, "w") as json_file:
        json_file.write("{\"fontHEX\": [\n")
        for i, line in enumerate(array_2d):
            json_file.write("[")
            for j, col in enumerate(line):
                json_file.write(str(int(col, 16)))
                if j != len(line) - 1:
                    json_file.write(",")
            json_file.write("],\n" if i != len(array_2d) - 1 else "]")
        json_file.write("]\n}\n")

    # End timer and display time taken
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Time taken: {elapsed_time:.6f} seconds")


if __name__ == '__main__':
    convert("C:/Users/Mari/Desktop/mario.png")
