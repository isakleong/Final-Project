def testData(text):
    import joblib
    # import joblib

    #import model
    filename = 'D:/Kuliah/Semester_8/Skripsi/Training Model/Model/model_v1.pkl'
    model = joblib.load(filename)
    result = model.predict_proba([text])[0]

    return result

import sys
# get_text = sys.argv[1]
string = ''
for word in sys.argv[1:]:
    string += word + ' '
# print(get_text)
import json
# print(json.dumps(testData(string)))
print(testData(string))
