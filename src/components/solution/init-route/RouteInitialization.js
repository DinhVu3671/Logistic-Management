import { CContainer } from '@coreui/react';
import React, { Component } from 'react'
import ParameterConfiguration from './ParameterConfiguration';
import ClusteringCustomers from './ClusteringCustomers';
import Progress from './Progress';
import customerService from '../../../service/CustomerService';
import depotService from '../../../service/DepotService';
import routeService from '../../../service/SolutionRouteService';
import SolutionRoute from '../SolutionRoute';
import randomColor from "randomcolor";
import orderService from '../../../service/OrderService';
import vehicleService from '../../../service/VehicleService';
import InitializationType from './InitializationType';

class RouteInitialization extends Component {

    constructor(props) {
        super(props);
        this.state = {
            step: 0,
            problemAssumption: {},
            parameterConfiguration: {},
            isProgress: false,
            isAllowedViolateTW: false,
            isExcludeProduct: false,
            // isLimitedTime
            // maxTravelTime
            // isLimitedDistance
            // maxDistance
            maxTime: 2,
            popSize: 75,
            eliteSize: 10,
            eliteRate: 13.3,
            maxGen: 75,
            maxGenImprove: 50,
            probCrossover: 0.96,
            probMutation: 0.16,
            tournamentSize: 20,
            selectionRate: 20,
            isInitFail: false,
            exponentialFactor: 1,
        };
        this.reInitialize = this.reInitialize.bind(this);
    }

    componentDidMount() {
        this.setState({ isWaiting: true });
        customerService.getClusteringCustomers().then(response => {
            const clusteringCustomers = response.data.data;
            this.setState({
                clusteringCustomers: clusteringCustomers,
            });
        }).catch(e => {
            console.log(e);
        });
        let search = { paged: false };
        depotService.search(search).then(response => {
            const depotsData = response.data.data;
            this.setState({
                depots: depotsData,
            });
        }).catch(e => {
            console.log(e);
        });

        orderService.search(search).then(response => {
            const ordersData = response.data.data;
            this.setState({
                orders: ordersData,
                isWaiting: false,
            });
        }).catch(e => {
            console.log(e);
        });

        search.available = true;
        vehicleService.search(search).then(response => {
            const vehiclesData = response.data.data;
            this.setState({
                vehicles: vehiclesData,
            });
        }).catch(e => {
            console.log(e);
        });
    }

    setProgressing = () => {
        this.setState({
            isProgress: true,
            isInitFail: false,
            progressPercent: 0,
        });
        const timer = setInterval(() => {
            let newProgressPercent = parseFloat(this.state.progressPercent + 0.5);
            if (newProgressPercent >= 100) {
                clearInterval(this.state.timer)
            }
            this.setState({
                progressPercent: newProgressPercent > 100 ? 100 : newProgressPercent,
            });
        }, 1000);
        this.setState({ timer: timer });
    }

    setDepots = () => {
        let search = { paged: false };
        depotService.search(search).then(response => {
            const depotsData = response.data.data;
            depotsData.map((depot) => {
                depot.marker = {
                    id: depot.id,
                    code: depot.code,
                    lat: depot.latitude,
                    lng: depot.longitude,
                    color: 'white',
                    isShowed: true,
                }
            });
            this.setState({
                depots: depotsData,
            });
        }).catch(e => {
            console.log(e);
        });
    }

    setCustomers = () => {
        let search = { paged: false };
        customerService.search(search).then(response => {
            const customersData = response.data.data;
            customersData.map((customer) => {
                customer.marker = {
                    id: customer.id,
                    code: customer.code,
                    lat: customer.latitude,
                    lng: customer.longitude,
                    isShowed: true,
                }
            });
            this.setState({
                customers: customersData,
            });

        }).catch(e => {
            console.log(e);
        });
    }

    processSolutions = (solutions) => {
        solutions = solutions.map((solution, index) => {
            solution.id = index + 1;
            solution.focusJourneyId = 0
            solution.efficiency = Math.round(solution.efficiency * 100) / 100;
            this.setDirections(solution);
            return solution;
        })
    }

    setDirections = (solution) => {
        let directions = [];
        solution.journeys.map((journey, journeyIndex) => {
            let directionColor = randomColor();
            let directionOfJourney = [];
            journey.routes.map((route, routeIndex) => {
                let directionArcs = route.arcs.map((arc, arcIndex) => {
                    return ({
                        id: 1000 * journeyIndex + 100 * routeIndex + arcIndex,
                        from: arc.fromNode,
                        to: arc.toNode,
                        strokeColor: directionColor,
                        isShowed: true,
                    });
                });
                directionOfJourney = directionOfJourney.concat(directionArcs);
            });
            directions = directions.concat(directionOfJourney);
            journey.directions = directionOfJourney;
        });
        solution.directions = directions;
    }

    handleGetRoutes = () => {
        this.setProgressing();
        this.setDepots();
        this.setCustomers();
        let problemAssumption = {
            isExcludeProduct: this.state.isExcludeProduct,
            isLimitedTime: this.state.isLimitedTime,
            maxTravelTime: this.state.maxTravelTime,
            isLimitedDistance: this.state.isLimitedDistance,
            isAllowedViolateTW: this.state.isAllowedViolateTW,
            maxDistance: this.state.maxDistance,
            maxTime: this.state.maxTime,
            popSize: this.state.popSize,
            eliteSize: this.state.eliteSize,
            eliteRate: this.state.eliteRate,
            exponentialFactor: this.state.exponentialFactor,
            maxGen: this.state.maxGen,
            maxGenImprove: this.state.maxGenImprove,
            probCrossover: this.state.probCrossover,
            probMutation: this.state.probMutation,
            tournamentSize: this.state.tournamentSize,
            selectionRate: this.state.selectionRate,
            customers: this.state.clusteringCustomers,
            depots: this.state.depots,
            vehicles: this.state.vehicles,
            orders: this.state.orders,

        };
        console.log("clusteringCustomers:", this.state.clusteringCustomers);
        console.log("problemAssumption:", problemAssumption);
        this.setState({
            problemAssumption: problemAssumption
        });
        routeService.getRoutes(problemAssumption).then(response => {
            console.log("problemAssumption:", problemAssumption);
            clearInterval(this.state.timer)
            if (response.data.code === "SUCCESS") {
                const solutions = response.data.data;
                this.processSolutions(solutions);
                console.log(solutions);
                this.setState({
                    progressPercent: 100,
                    solution: solutions[0],
                    solutions: solutions,
                    step: 4,
                });
                console.log(solutions);
                console.log("problemAssumption:", problemAssumption);
            } else {
                console.log("response:", response);
                this.setState({
                    progressPercent: 100,
                    step: 3,
                });
            }
            // this.goToStep(4);
        }).catch(e => {
            console.log(e);
            clearInterval(this.state.timer)
            this.setState({
                isProgress: false,
                isInitFail: true,
                step: 3,
            });
        });
        // this.setState({ step: 4 });
        console.log("this.state.problemAssumption:", problemAssumption);

    }

    nextStep = () => {
        const { step } = this.state
        this.setState({
            step: step + 1
        })
    }

    prevStep = () => {
        const { step } = this.state
        this.setState({
            step: step - 1
        })
    }

    goToStep = (step) => {
        this.setState({
            step: step
        });
        if (step === 1 || step === 0)
            this.setState({
                isInitFail: false
            });
    }

    handleReInitWithoutConstraint = () => {
        this.setState({
            isAllowedViolateTW: true,
        });
        this.setProgressing();
        this.setDepots();
        this.setCustomers();
        let problemAssumption = {
            isExcludeProduct: this.state.isExcludeProduct,
            isLimitedTime: this.state.isLimitedTime,
            maxTravelTime: this.state.maxTravelTime,
            isLimitedDistance: this.state.isLimitedDistance,
            isAllowedViolateTW: true,
            maxDistance: this.state.maxDistance,
            maxTime: this.state.maxTime,
            popSize: this.state.popSize,
            eliteSize: this.state.eliteSize,
            eliteRate: this.state.eliteRate,
            exponentialFactor: this.state.exponentialFactor,
            maxGen: this.state.maxGen,
            maxGenImprove: this.state.maxGenImprove,
            probCrossover: this.state.probCrossover,
            probMutation: this.state.probMutation,
            tournamentSize: this.state.tournamentSize,
            selectionRate: this.state.selectionRate,
            customers: this.state.clusteringCustomers,
            depots: this.state.depots,
            vehicles: this.state.vehicles,
            orders: this.state.orders,

        };
        console.log("clusteringCustomers:", this.state.clusteringCustomers);
        console.log("problemAssumption:", problemAssumption);
        this.setState({
            problemAssumption: problemAssumption
        });
        routeService.getRoutes(problemAssumption).then(response => {
            console.log("problemAssumption:", problemAssumption);
            clearInterval(this.state.timer)
            if (response.data.code === "SUCCESS") {
                const solutions = response.data.data;
                this.processSolutions(solutions);
                console.log(solutions);
                this.setState({
                    progressPercent: 100,
                    solution: solutions[0],
                    solutions: solutions,
                    step: 4,
                });
                console.log(solutions);
                console.log("problemAssumption:", problemAssumption);
            } else {
                console.log("response:", response);
                this.setState({
                    progressPercent: 100,
                    step: 3,
                });
            }
            // this.goToStep(4);
        }).catch(e => {
            console.log(e);
            clearInterval(this.state.timer)
            this.setState({
                isProgress: false,
                isInitFail: true,
                step: 3,
            });
        });
    }

    reInitialize() {
        window.location.reload();
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    changeParameterConfiguration(parameterConfiguration) {
        this.setState({ parameterConfiguration: parameterConfiguration });
    }

    changeClusteringCustomers(clusteringCustomers) {
        this.setState({ clusteringCustomers: clusteringCustomers });
    }

    changeProgress(progress) {
        this.setState({ progress: progress });
    }

    render() {
        const { isInitFail, isLimitedTime, maxTravelTime, isLimitedDistance, maxDistance,
            isProgress, progressPercent, clusteringCustomers, depots, isAllowedViolateTW,
            isExcludeProduct, maxTime, popSize, eliteSize, maxGen, maxGenImprove, probCrossover,
            probMutation, tournamentSize, selectionRate, eliteRate, exponentialFactor } = this.state;
        const values = {
            isInitFail, isLimitedTime, maxTravelTime, isLimitedDistance, maxDistance,
            isProgress, progressPercent, clusteringCustomers, depots, isAllowedViolateTW,
            isExcludeProduct, maxTime, popSize, eliteSize, maxGen, maxGenImprove, probCrossover,
            probMutation, tournamentSize, selectionRate, eliteRate, exponentialFactor
        };
        switch (this.state.step) {
            case 0:
                return <InitializationType
                    goToStep={this.goToStep}
                />
            case 1:
                return <ParameterConfiguration
                    nextStep={this.nextStep}
                    handleInputChange={this.handleInputChange}
                    values={values}
                    isWaiting={this.state.isWaiting}
                />
            case 2:
                return <ClusteringCustomers
                    changeClusteringCustomers={this.changeClusteringCustomers}
                    prevStep={this.prevStep}
                    nextStep={this.nextStep}
                    values={values} />
            case 3:
                return <Progress
                    changeProgress={this.changeProgress}
                    handleGetRoutes={this.handleGetRoutes}
                    prevStep={this.prevStep}
                    goToStep={this.goToStep}
                    values={values}
                    handleReInitWithoutConstraint={this.handleReInitWithoutConstraint}
                    isWaiting={this.state.isWaiting} />
            case 4:
                return <SolutionRoute
                    reInitialize={this.reInitialize}
                    depots={this.state.depots}
                    customers={this.state.customers}
                    orders={this.state.orders}
                    vehicles={this.state.vehicles}
                    solutions={this.state.solutions}
                    problemAssumption={this.state.problemAssumption}
                    values={values} />
            default:
                return;
        }
    }
}

export default RouteInitialization;
