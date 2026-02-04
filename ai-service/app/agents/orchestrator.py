"""
Orchestrator Agent - Coordinates all specialized agents and makes final decisions.
Implements the Hybrid Architecture with Late Fusion approach.
"""
from typing import Dict, Any, List
import numpy as np
from .imaging_agent import ImagingAgent
from .genomic_agent import GenomicAgent
from .clinical_agent import ClinicalAgent
import logging

logger = logging.getLogger(__name__)


class OrchestratorAgent:
    """
    Main orchestrator that coordinates specialized agents and produces final diagnosis.
    Uses late fusion (ensemble) approach to combine predictions.
    """
    
    def __init__(self):
        self.imaging_agent = ImagingAgent()
        self.genomic_agent = GenomicAgent()
        self.clinical_agent = ClinicalAgent()
        
        # Weights for each agent in the ensemble (can be tuned)
        self.agent_weights = {
            "imaging": 0.45,  # Imaging is often most important
            "genomic": 0.35,  # Genetic factors are significant
            "clinical": 0.20  # Clinical factors provide context
        }
        
        logger.info("OrchestratorAgent initialized with all specialized agents")
    
    async def diagnose(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Perform comprehensive breast cancer diagnosis using all available data.
        
        Args:
            data: Dictionary containing:
                - imaging_data: Medical images and metadata
                - genomic_data: Genetic and molecular data
                - clinical_data: Patient clinical information
                - patient_id: Optional patient identifier
        
        Returns:
            Comprehensive diagnosis with predictions from all agents and final decision
        """
        logger.info("=" * 80)
        logger.info("Starting comprehensive breast cancer diagnosis")
        logger.info("=" * 80)
        
        patient_id = data.get("patient_id", "unknown")
        
        # Collect results from all agents
        agent_results = {}
        
        # 1. Imaging Analysis
        if "imaging_data" in data and data["imaging_data"]:
            try:
                imaging_result = await self.imaging_agent.analyze(data["imaging_data"])
                agent_results["imaging"] = imaging_result
                logger.info(f"Imaging analysis complete: {imaging_result['prediction']}")
            except Exception as e:
                logger.error(f"Imaging analysis failed: {str(e)}")
                agent_results["imaging"] = {"error": str(e), "prediction": None}
        
        # 2. Genomic Analysis
        if "genomic_data" in data and data["genomic_data"]:
            try:
                genomic_result = await self.genomic_agent.analyze(data["genomic_data"])
                agent_results["genomic"] = genomic_result
                logger.info(f"Genomic analysis complete: {genomic_result['prediction']}")
            except Exception as e:
                logger.error(f"Genomic analysis failed: {str(e)}")
                agent_results["genomic"] = {"error": str(e), "prediction": None}
        
        # 3. Clinical Analysis
        if "clinical_data" in data and data["clinical_data"]:
            try:
                clinical_result = await self.clinical_agent.analyze(data["clinical_data"])
                agent_results["clinical"] = clinical_result
                logger.info(f"Clinical analysis complete: {clinical_result['prediction']}")
            except Exception as e:
                logger.error(f"Clinical analysis failed: {str(e)}")
                agent_results["clinical"] = {"error": str(e), "prediction": None}
        
        # 4. Ensemble Decision Making
        final_decision = self._make_ensemble_decision(agent_results)
        
        # 5. Generate comprehensive report
        comprehensive_report = self._generate_comprehensive_report(
            agent_results, 
            final_decision, 
            patient_id
        )
        
        logger.info("=" * 80)
        logger.info(f"Final diagnosis: {final_decision['diagnosis']} (confidence: {final_decision['confidence']:.2%})")
        logger.info("=" * 80)
        
        return comprehensive_report
    
    def _make_ensemble_decision(self, agent_results: Dict[str, Dict[str, Any]]) -> Dict[str, Any]:
        """
        Combine predictions from all agents using weighted ensemble.
        
        Args:
            agent_results: Dictionary of results from each agent
        
        Returns:
            Final decision with diagnosis, confidence, and risk score
        """
        logger.info("Making ensemble decision...")
        
        # Collect valid predictions
        predictions = []
        confidences = []
        weights = []
        
        for agent_name, result in agent_results.items():
            if "error" not in result and result.get("prediction") is not None:
                predictions.append(result["prediction"])
                confidences.append(result["confidence"])
                weights.append(self.agent_weights.get(agent_name, 0.33))
        
        if not predictions:
            logger.warning("No valid predictions available")
            return {
                "diagnosis": "inconclusive",
                "prediction": None,
                "confidence": 0.0,
                "risk_score": 0.0,
                "consensus_level": "none"
            }
        
        # Normalize weights
        total_weight = sum(weights)
        normalized_weights = [w / total_weight for w in weights]
        
        # Calculate weighted average of predictions
        weighted_prediction = sum(p * w * c for p, w, c in zip(predictions, normalized_weights, confidences))
        
        # Calculate overall confidence
        overall_confidence = sum(c * w for c, w in zip(confidences, normalized_weights))
        
        # Determine final prediction
        final_prediction = 1 if weighted_prediction > 0.5 else 0
        
        # Calculate risk score (0-100)
        risk_score = weighted_prediction * 100
        
        # Determine diagnosis
        if final_prediction == 1:
            if risk_score > 75:
                diagnosis = "malignant - high confidence"
            elif risk_score > 60:
                diagnosis = "malignant - moderate confidence"
            else:
                diagnosis = "suspicious - further investigation recommended"
        else:
            if risk_score < 25:
                diagnosis = "benign - high confidence"
            elif risk_score < 40:
                diagnosis = "benign - moderate confidence"
            else:
                diagnosis = "indeterminate - further investigation recommended"
        
        # Calculate consensus level
        consensus_level = self._calculate_consensus(predictions)
        
        decision = {
            "diagnosis": diagnosis,
            "prediction": final_prediction,
            "confidence": float(overall_confidence),
            "risk_score": float(risk_score),
            "consensus_level": consensus_level,
            "agent_agreement": self._calculate_agreement(predictions)
        }
        
        logger.info(f"Ensemble decision: {diagnosis} (risk score: {risk_score:.1f}%)")
        return decision
    
    def _calculate_consensus(self, predictions: List[int]) -> str:
        """Calculate consensus level among agents."""
        if not predictions:
            return "none"
        
        agreement = sum(predictions) / len(predictions)
        
        if agreement == 1.0 or agreement == 0.0:
            return "unanimous"
        elif agreement >= 0.66 or agreement <= 0.33:
            return "strong"
        else:
            return "weak"
    
    def _calculate_agreement(self, predictions: List[int]) -> float:
        """Calculate percentage agreement among agents."""
        if not predictions:
            return 0.0
        
        majority = max(predictions.count(0), predictions.count(1))
        return (majority / len(predictions)) * 100
    
    def _generate_comprehensive_report(
        self, 
        agent_results: Dict[str, Dict[str, Any]], 
        final_decision: Dict[str, Any],
        patient_id: str
    ) -> Dict[str, Any]:
        """Generate comprehensive diagnosis report."""
        
        # Extract key findings from each agent
        key_findings = []
        
        for agent_name, result in agent_results.items():
            if "error" not in result and "explanation" in result:
                key_findings.append({
                    "agent": agent_name,
                    "finding": result["explanation"]
                })
        
        # Generate recommendations
        recommendations = self._generate_recommendations(final_decision, agent_results)
        
        # Compile comprehensive report
        report = {
            "patient_id": patient_id,
            "diagnosis_summary": {
                "final_diagnosis": final_decision["diagnosis"],
                "risk_score": final_decision["risk_score"],
                "confidence": final_decision["confidence"],
                "consensus_level": final_decision["consensus_level"],
                "agent_agreement_percentage": final_decision.get("agent_agreement", 0)
            },
            "agent_analyses": agent_results,
            "key_findings": key_findings,
            "recommendations": recommendations,
            "interpretation": self._generate_interpretation(final_decision, agent_results)
        }
        
        return report
    
    def _generate_recommendations(
        self, 
        final_decision: Dict[str, Any], 
        agent_results: Dict[str, Dict[str, Any]]
    ) -> List[str]:
        """Generate clinical recommendations based on diagnosis."""
        
        recommendations = []
        risk_score = final_decision["risk_score"]
        
        if risk_score > 75:
            recommendations.append("Immediate biopsy recommended")
            recommendations.append("Consultation with oncologist advised")
            recommendations.append("Consider genetic counseling if BRCA mutations detected")
        elif risk_score > 50:
            recommendations.append("Biopsy recommended for definitive diagnosis")
            recommendations.append("Short-term follow-up imaging (3-6 months)")
            recommendations.append("Multidisciplinary team review suggested")
        elif risk_score > 25:
            recommendations.append("Close monitoring with follow-up imaging in 6 months")
            recommendations.append("Consider additional imaging modalities (MRI, ultrasound)")
        else:
            recommendations.append("Routine screening schedule appropriate")
            recommendations.append("Annual mammography as per guidelines")
            recommendations.append("Maintain healthy lifestyle and risk reduction strategies")
        
        # Add specific recommendations based on agent findings
        if "genomic" in agent_results:
            genomic_result = agent_results["genomic"]
            if not genomic_result.get("error"):
                risk_genes = genomic_result.get("risk_genes", [])
                if "BRCA1" in risk_genes or "BRCA2" in risk_genes:
                    recommendations.append("Genetic counseling strongly recommended")
                    recommendations.append("Consider risk-reducing strategies")
        
        return recommendations
    
    def _generate_interpretation(
        self, 
        final_decision: Dict[str, Any], 
        agent_results: Dict[str, Dict[str, Any]]
    ) -> str:
        """Generate human-readable interpretation of the diagnosis."""
        
        diagnosis = final_decision["diagnosis"]
        risk_score = final_decision["risk_score"]
        consensus = final_decision["consensus_level"]
        
        interpretation = f"Based on comprehensive multimodal analysis, the diagnosis is: {diagnosis}. "
        interpretation += f"The calculated risk score is {risk_score:.1f}% with {consensus} consensus among specialized agents. "
        
        # Add context from each agent
        agent_contexts = []
        for agent_name, result in agent_results.items():
            if "error" not in result and result.get("prediction") is not None:
                pred_text = "supports malignancy" if result["prediction"] == 1 else "suggests benign findings"
                conf = result["confidence"]
                agent_contexts.append(f"{agent_name} analysis {pred_text} (confidence: {conf:.0%})")
        
        if agent_contexts:
            interpretation += " ".join(agent_contexts) + ". "
        
        # Add final note
        if consensus == "unanimous":
            interpretation += "All agents are in agreement, providing high reliability in this assessment."
        elif consensus == "weak":
            interpretation += "There is some disagreement among agents, suggesting the case may benefit from additional investigation."
        
        return interpretation
