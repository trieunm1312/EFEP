// const xValues = ["Italy", "France", "Spain", "USA", "Argentina"];
// const yValues = [55, 49, 44, 24, 15];
// const barColors = [
//   "#b91d47",
//   "#00aba9",
//   "#2b5797",
//   "#e8c3b9",
//   "#1e7145"
// ];

// new Chart("myChart", {
//   type: "pie",
//   data: {
//     labels: xValues,
//     datasets: [{
//       backgroundColor: barColors,
//       data: yValues
//     }]
//   },
//   options: {
//     title: {
//       display: true,
//       text: "World Wide Wine Production 2018"
//     }
//   }
// });

const xValues = orderCounts.map(order => order.date);
const yValues = orderCounts.map(order => order.count);

new Chart("dailyOrderChart", {
    type: "line",
    data: {
        labels: xValues,
        datasets: [{
            fill: false,
            lineTension: 0,
            backgroundColor: "pink",
            borderColor: "red",
            data: yValues
        }]
    },
    options: {
        legend: { display: false },
        scales: {
            yAxes: [{ ticks: { min: 0, max: 20} }],
        },
        title: {
            display: true,
            text: "Daily Order Chart"
        }
    }
});



// 
const categoryName = soldQuantityCategory.map(quantity => quantity.category);
const categorySold = soldQuantityCategory.map(quantity => quantity.soldFlowerQuantity);
const barColors = ["red", "green","blue","orange","brown","pink","purple"];

new Chart("compareFlowerCategoryChart", {
  type: "bar",
  data: {
    labels: categoryName,
    datasets: [{
      backgroundColor: barColors,
      data: categorySold
    }]
  },
  options: {
    legend: {display: false},
      scales: {
          yAxes: [{ ticks: { beginAtZero: true, stepSize: 1 } }],
          xAxes: [{ barPercentage: 0.5 }] // Optional: controls the width of the bars
      },
    title: {
      display: true,
      text: "Compare Flower Category Sold Quantity"
    }
  }
});
