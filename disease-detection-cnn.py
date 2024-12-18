# CNN model deployment
# pip install python-multipart


from fastapi import File, UploadFile 
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
import io # input output işlemleri için kullanılır
from fastapi import FastAPI, HTTPException, Depends
import sqlite3
from fastapi.responses import HTMLResponse, JSONResponse

# FastAPI uygulamasını başlat
app = FastAPI()

# @ -> decorator -> bir fonksiyonu başka bir fonksiyonla genişletmek için kullanılır
@app.get("/") # / endpointi için GET metodu 
def home():
    return {"message": "Plant Disease Detection!"}

# /about endpointi için bir url yönlendirmesi oluşturalım
@app.get("/about") # /about endpointi için GET metodu
def about():
    return {"message": "FastAPI."}

# ekrana isim yazdıran bir endpoint oluşturalım
@app.get("/isim/") # /isim endpointi için GET metodu
def isim(name:str): # name sadece string olabilir başka bir veri tipi girilirse hata verir(fakat str herhangi bir veri tipi olabilir)
    return {"message": f"Merhaba, {name}!"}

@app.post("/predict_image/")
async def predict_image(file: UploadFile = File(...)): # dosya yükleme işlemi
    load_ann = load_model('trained_model.h5')
    contents = await file.read() # dosya içeriğini oku
    img = image.load_img(io.BytesIO(contents), target_size=(128, 128)) # resmi yükle ve boyutlandır
    img_array = image.img_to_array(img) # resmi array yapısına çevir
    img_array = np.expand_dims(img_array, axis=0) # boyutlandırma işlemi
    prediction = load_ann.predict(img_array) # tahmin yap
    predicted_class_index = np.argmax(prediction[0]) # en yüksek değeri al

    class_labels = ['Apple: scab',
                    'Apple: Black_rot',
 'Apple: Cedar apple rust',
 'Apple: healthy',
 'Blueberry: healthy',
 'Cherry: Powdery mildew',
 'Cherry: Healthy',
 'Corn: Cercospora leaf spot, gray leaf spot',
 'Corn: Common_rust_',
 'Corn: Northern leaf blight',
 'Corn:healthy',
 'Grape: Black_rot',
 'Grape: Esca (Black Measles)',
 'Grape: Leaf blight (Isariopsis Leaf Spot)',
 'Grape: healthy',
 'Orange: Haunglongbing (Citrus greening)',
 'Peach: Bacterial spot',
 'Peach: healthy'] # sınıf etiketleri

    prediction = class_labels[predicted_class_index] # tahmin edilen sınıfı al
    return JSONResponse(content= {"prediction": prediction})
    #html_content = f"<html><body><h1>Prediction: {prediction}</h1></body></html>"
    #return HTMLResponse(content=html_content)
   # return {"prediction": prediction}




@app.get("/")
async def read_root():
    return {"message": "Welcome to the Churn Prediction API."}



# Run the FastAPI app
# python -m uvicorn ann:app --reload