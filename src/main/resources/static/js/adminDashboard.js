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


// Generate xValues (months) and yValues (order counts) from orderCounts data
const xValues = orderCounts.map(order => order.month);
const yValues = orderCounts.map(order => order.count);

// Create the chart as a bar chart
new Chart("monthlyOrderChart", {
    type: "bar",
    data: {
        labels: xValues,
        datasets: [{
            backgroundColor: "pink",
            borderColor: "red",
            data: yValues
        }]
    },
    options: {
        legend: { display: false },
        scales: {
            yAxes: [{ ticks: { beginAtZero: true } }],
            xAxes: [{ barPercentage: 0.5 }] // Optional: controls the width of the bars
        },
        title: {
            display: true,
            text: "Monthly Order Chart"
        }
    }
});

const userData = document.getElementById("userData");
const totalBuyer = parseInt(userData.getAttribute("data-total-buyer"));
const totalSeller = parseInt(userData.getAttribute("data-total-seller"));

const userTypes = ["Buyer", "Seller"];
const number = [totalBuyer, totalSeller];
const userColors = [
  "#b91d47",
  "#00aba9"
];

new Chart("compareUserTypeChart", {
  type: "pie",
  data: {
    labels: userTypes,
    datasets: [{
      backgroundColor: userColors,
      data: number
    }]
  },
  options: {
    title: {
      display: true,
      text: "World Wide Wine Production 2018"
    }
  }
});
