# media/screens

Сюда нужно положить изображения, на которые ссылается корневой `README.md`.
Их можно получить из Allure-отчёта после прогона (`allure serve build/allure-results`).

Требуемые файлы (имена важны — именно они прописаны в README):

| Файл | Что на нём | Где взять |
|------|------------|-----------|
| `allure-overview.png` | Дашборд Allure (Overview) со сводкой 7/7 passed | вкладка **Overview** в отчёте |
| `allure-test-result.png` | Раскрытый результат одного теста: шаги + скриншот | любой тест → вкладка со **Steps** и вложениями |
| `selenoid-video.gif` | Короткая запись прогона теста | вложение **Video** у теста (Selenoid), сконвертировать `.mp4` → `.gif` |

Как быстро снять:
1. `./gradlew clean test` — прогнать тесты (в Selenoid, чтобы было видео).
2. `allure serve build/allure-results` — открыть отчёт в браузере.
3. Сделать скриншоты Overview и результата теста, сохранить под именами выше.
4. Видео теста скачать (`https://selenoid.qa.guru/video/<sessionId>.mp4`) и сконвертировать в gif,
   например: `ffmpeg -i video.mp4 -vf "fps=10,scale=960:-1" selenoid-video.gif`
