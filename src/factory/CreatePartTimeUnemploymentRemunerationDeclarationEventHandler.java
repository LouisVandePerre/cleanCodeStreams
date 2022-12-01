//package factory;
//
//import be.everesst.framework.core.eventbus.api.annotations.UseCaseEventHandler;
//import be.everesst.periodlibrary.period.ClosedPeriod;
//import be.everesst.socialriskdeclaration.configuration.core.domain.AsrConfigurationRepository;
//import be.everesst.socialriskdeclaration.employment.core.domain.employee.EmployeeId;
//import be.everesst.socialriskdeclaration.employment.core.domain.employee.Ssin;
//import be.everesst.socialriskdeclaration.employment.core.domain.employment.Employment;
//import be.everesst.socialriskdeclaration.employment.core.domain.employment.EmploymentRepository;
//import be.everesst.socialriskdeclaration.employment.core.domain.involuntaryparttimes.InvoluntaryPartTimes;
//import be.everesst.socialriskdeclaration.employment.core.domain.involuntaryparttimes.InvoluntaryPartTimesRepository;
//import be.everesst.socialriskdeclaration.employment.core.domain.workerrecord.EmployeeWorkerRecords;
//import be.everesst.socialriskdeclaration.employment.core.domain.workerrecord.EmployeeWorkerRecordsFactory;
//import be.everesst.socialriskdeclaration.wage.core.domain.WageCalculation;
//import be.everesst.socialriskdeclaration.wage.core.domain.WageCalculationRepository;
//import be.everesst.socialriskdeclaration.wage.core.domain.employeewages.EmployeeWages;
//import be.everesst.socialriskdeclaration.wage.core.driving.port.event.CompleteWageFinalizationForEmployeeFinalizedEvent;
//import be.everesst.socialriskdeclaration.wech.core.domain.parttimeunemployment.remuneration.PartTimeUnemploymentRemunerationDeclaration;
//import be.everesst.socialriskdeclaration.wech.core.domain.parttimeunemployment.remuneration.PartTimeUnemploymentRemunerationDeclarationRepository;
//import be.everesst.socialriskdeclaration.wech.core.domain.parttimeunemployment.remuneration.PartTimeUnemploymentRemunerationRiskIdRepository;
//import be.everesst.socialriskdeclaration.wech.core.usecase.parttimeunemployment.remuneration.mapping.PartTimeUnemploymentRemunerationDeclarationNssoTOMapper;
//import be.everesst.socialriskdeclaration.wech.driven.port.gateway.payloadgenerator.DeclarationPayloadGenerator;
//import be.everesst.socialriskdeclaration.wech.driven.port.gateway.payloadgenerator.parttimeunemployment.remuneration.PartTimeUnemploymentRemunerationDeclarationNssoTO;
//
//import javax.inject.Named;
//import java.util.*;
//
//import static be.everesst.socialriskdeclaration.wech.core.usecase.parttimeunemployment.remuneration.PartTimeRemunerationDeclarationReferencePeriodFactory.determineReferencePeriodsOfWech006DeclarationsToCreate;
//import static factory.PartTimeRemunerationDeclarationReferencePeriodFactory.determineReferencePeriodsOfWech006DeclarationsToCreate;
//
//@Named
//public class CreatePartTimeUnemploymentRemunerationDeclarationEventHandler {
//
//
//    private void persistRemunerationDeclaration(Employment employment,
//                                                InvoluntaryPartTimes involuntaryPartTimes,
//                                                WageCalculation wageCalculation,
//                                                EmployeeWages employeeWages) {
//        EmployeeWorkerRecords employeeWorkerRecords = employeeWorkerRecordsFactory.createEmployeeWorkerRecordsUsingFinalizedAndProvisionalWorkedRecords(
//                employment.getAgreementIds(),
//                wageCalculation.getWagePeriod().getClosedEndDate().plusDays(1)
//        );
//
////        PRE REFACTORING -> Works but not readable/clean => extract to methods then extract to separate factory class to split te responsibility up
////
////        /**
////         * 1/1 - 10/1 Part time time credit (4)
////         * 11/1 - 20/1 Full time time credit (3)
////         * 21/1 - 31/1 Part time time credit (4)
////         * --> 3 WECH006
////         */
//        private List<ClosedPeriod> determineReferencePeriodsOfWech006DeclarationsToCreate(EmployeeWorkerRecords employeeWorkerRecords, WageCalculation wageCalculation) {
//            var occupationsForWagePeriod = employeeWorkerRecords
//                    .findWithOverlappingOccupation(wageCalculation.getWagePeriod())
//                    .getOccupationOverlappingWith(wageCalculation.getWagePeriod())
//                    .stream()
//                    .sorted(Comparator.comparing(OccupationHistoryItem::getOccupationPeriod))
//                    .toList()
//                    .iterator();
//
//            var periods = new LinkedList<ClosedPeriod>();
//            OccupationHistoryItem previousWorkerRecord = null;
//            while (occupationsForWagePeriod.hasNext()) {
//                var nextOccupation = occupationsForWagePeriod.next();
//                var previousReorg = previousWorkerRecord != null ? previousWorkerRecord.getReorganisationMeasure().orElse(null) : null;
//                var nextReorg = nextOccupation.getReorganisationMeasure().orElse(null);
//                if (previousReorg != null && (FULL_TIME_TIME_CREDIT_OR_THEMATIC_LEAVE.equals(previousReorg) ^ FULL_TIME_TIME_CREDIT_OR_THEMATIC_LEAVE.equals(nextReorg))) {
//                    periods.add(nextOccupation.getOccupationPeriod());
//                } else {
//                    ClosedPeriod previousPeriod = periods.pollLast();
//                    periods.add(ClosedPeriod.periodOf(previousPeriod == null ? nextOccupation.getStartDate() : previousPeriod.getClosedStartDate(), nextOccupation.getEndDate()));
//
//                }
//                previousWorkerRecord = nextOccupation;
//            }
//
//            return periods;
//        }
//
//        determineReferencePeriodsOfWech006DeclarationsToCreate(employeeWorkerRecords, wageCalculation.getWagePeriod())
//                .forEach(referencePeriod ->
//                        PartTimeUnemploymentRemunerationDeclaration.create(
//                                employment,
//                                wageCalculation,
//                                involuntaryPartTimes,
//                                employeeWages,
//                                asrConfigurationRepository,
//                                riskIdRepository,
//                                employeeWorkerRecords,
//                                referencePeriod,
//                                this::validatePayload
//                        ).ifPresent(declarationRepository::save));
//    }
//
//}
