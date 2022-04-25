/**
* Variables using in script
*/
var index;
var currentQuestion;
var questionList = [];
var answers = [];
var userAnswers = [];
var uncompleted = true;

/**
* Function loads on activity.html body load.
*/

function startQuiz(questions) {
  console.log("starting quiz");
  questionList = questions;
  index = 0;
  answers = [];
  document.getElementById("set_question").innerHTML = questionList[0];
  setProgressBar(index);
}

/**
* Function loads next question in questionList array.
*/

function nextQuestion() {
  var answer = parseInt(document.getElementById("answer").value, 10);
  console.log(answer, typeof answer);
  if (isNaN(answer)) {
    swal("no has introducido ningún número");
  } else {
    answers.push(evaluate(answer, questionList[index]));
    userAnswers.push(answer);
    console.log(answers, userAnswers);
    index++;
    setProgressBar(index);
    document.getElementById("answer").value = "";
    if (index == 10) submitExercise();
    else setQuestion(index);
  }
}

/**
* Function sets question on demand.
*/

function setQuestion(index) {
  document.getElementById("set_question").innerHTML = questionList[index];
}

/**
* Function gets a operation string and compares it to user answer.
*
* @returns: boolean
*/

function evaluate(answer, question) {
  var auxQuestion = question.replace("x", "*");
  auxQuestion = auxQuestion.replace(".", "");
  auxQuestion = auxQuestion.replace(":", "/");
  //
  console.log("auxQuestion", auxQuestion);
  if (answer == eval(auxQuestion)) return true;
  return false;
}

/**
* Function shows post buttons when activity is finished
*/

function submitExercise() {
  var correctAnswers = answers.filter((item) => item == true).length;
  console.log("correct answers", correctAnswers);
  document.getElementById("board").style.visibility = "hidden";
  document.getElementById("board").innerHTML = "";
  document.getElementById("score_table").innerHTML = showScoreTable();

  if (correctAnswers > 8) {
    document.getElementById("first_case").style.visibility = "visible";
    document.getElementById("success_message").innerHTML =
      "Has superado esta actividad, puedes ver tus respuestas abajo.";
  } else {
    document.getElementById("second_case").style.visibility = "visible";
    document.getElementById("unsuccess_message").innerHTML =
      "No has superado esta actividad, puedes ver tus respuestas abajo.";
  }
}

/**
* Function paints an score table when exercise is finished.
*/

function showScoreTable() {
  var spanishScore = [];
  for (let i = 0; i < answers.length; i++) {
    if (answers[i]) spanishScore.push("correcto");
    else spanishScore.push("incorrecto");
  }
  var table = "<table class='table'>";
  table +=
    "<thead><tr><th>Pregunta</th><th>Tu respuesta</th><th>Calificación</th></tr></thead>";
  for (let i = 0; i < questionList.length; i++) {
    table += `<tr><td>${questionList[i]}</td><td>${userAnswers[i]}</td><td>${spanishScore[i]}</td></tr>`;
  }
  table += "</table>";
  return table;
}

/**
* Function sets pencil icons in progress bar.
*/

function setProgressBar(index) {
  console.log("changing bar shape", index);
  var progress = "";

  for (var i = 0; i < index; i++) {
    progress +=
      '<i class="fa fa-pencil" style="font-size:48px;color:blue"></i>';
  }
  for (var i = index; i < 10; i++) {
    progress +=
      '<i class="fa fa-pencil" style="font-size:48px;color:gray"></i>';
  }
  console.log(progress);
  document.getElementById("progress_bar").innerHTML = progress;
}
