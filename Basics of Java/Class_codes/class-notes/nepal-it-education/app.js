const detailButtons = document.querySelectorAll("[data-toggle-target]");
const progressItems = document.querySelectorAll("[data-progress-item]");
const progressFill = document.querySelector("[data-progress-fill]");
const progressText = document.querySelector("[data-progress-text]");

detailButtons.forEach((button) => {
  button.addEventListener("click", () => {
    const targetId = button.getAttribute("data-toggle-target");
    const target = document.getElementById(targetId);

    if (!target) {
      return;
    }

    const isOpen = target.classList.toggle("is-open");
    button.textContent = isOpen ? "Hide focus details" : "Read focus details";
  });
});

function updateProgress() {
  const total = progressItems.length;
  const completed = Array.from(progressItems).filter((item) => item.checked).length;
  const percent = total === 0 ? 0 : Math.round((completed / total) * 100);

  if (progressFill) {
    progressFill.style.width = `${percent}%`;
  }

  if (progressText) {
    progressText.textContent = `${completed} of ${total} actions selected.`;
  }
}

progressItems.forEach((item) => {
  item.addEventListener("change", updateProgress);
});

updateProgress();
