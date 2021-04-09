from os import path
import cv2
from datetime import datetime as dt
cam = cv2.VideoCapture(0)
cv2.namedWindow("test")
while True:
    _, frame = cam.read()
    k = cv2.waitKey(1)
    if k % 256 == 27:  # ESC pressed escaping
        break
    elif k % 256 == 32:  # SPACE pressed taking photo
        now = dt.now()
        now = now.strftime("%Y_%d_%H_%M_%S")

        desktopPath = path.expanduser("~/Desktop")
        cv2.imwrite(desktopPath + "/cvImage"+now+".png", frame)
        break
    font = cv2.FONT_HERSHEY_SIMPLEX
    frame = cv2.putText(frame, "Press 'Space' to take photo", (10, 20), font, .5, (0, 0, 255), 1, cv2.LINE_AA)
    frame = cv2.putText(frame, "ESC to exit", (10, 40), font, .5, (0, 0, 255), 1, cv2.LINE_AA)
    cv2.imshow("test", frame)

cam.release()
cv2.destroyAllWindows()
