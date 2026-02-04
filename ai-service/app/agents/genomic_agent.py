"""
Genomic Agent for analyzing genetic and molecular data.
Analyzes gene expressions, mutations, and genetic markers.
"""
from typing import Dict, Any
import numpy as np
from .base_agent import BaseAgent
import logging

logger = logging.getLogger(__name__)


class GenomicAgent(BaseAgent):
    """Agent specialized in analyzing genomic and genetic data."""
    
    def __init__(self):
        super().__init__(agent_name="GenomicAgent", confidence_threshold=0.65)
        # Key breast cancer related genes
        self.key_genes = ["BRCA1", "BRCA2", "TP53", "PTEN", "STK11", "CDH1", "PALB2", "ATM"]
        logger.info(f"GenomicAgent initialized monitoring {len(self.key_genes)} key genes")
    
    async def analyze(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analyze genomic data for breast cancer risk factors.
        
        Args:
            data: Dictionary containing:
                - gene_expressions: Dictionary of gene expression levels
                - mutations: List of detected mutations
                - genetic_markers: Additional genetic markers
        
        Returns:
            Analysis results with prediction, confidence, and genetic features
        """
        logger.info(f"{self.agent_name}: Starting genomic analysis")
        
        # Extract and analyze genetic features
        features = self._extract_genetic_features(data)
        prediction, confidence = self._classify_genetic_risk(features)
        
        # Generate explanation
        explanation = self._generate_explanation(features, prediction, confidence)
        
        result = {
            "agent": self.agent_name,
            "prediction": prediction,
            "confidence": float(confidence),
            "confidence_level": self.get_confidence_level(confidence),
            "features": features,
            "explanation": explanation,
            "risk_genes": self._identify_risk_genes(features)
        }
        
        logger.info(f"{self.agent_name}: Analysis complete - Prediction: {prediction}, Confidence: {confidence:.2f}")
        return result
    
    def _extract_genetic_features(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Extract relevant genetic features from genomic data.
        
        In production, this would analyze actual gene expression data,
        mutation profiles, and genetic markers.
        """
        # Simulate genetic feature extraction
        gene_expressions = data.get("gene_expressions", {})
        mutations = data.get("mutations", [])
        
        features = {
            "brca1_mutation": np.random.random() > 0.9,  # BRCA1 mutations are rare
            "brca2_mutation": np.random.random() > 0.9,
            "tp53_mutation": np.random.random() > 0.85,
            "her2_amplification": np.random.random(),
            "er_positive": np.random.random() > 0.3,  # Estrogen receptor
            "pr_positive": np.random.random() > 0.4,  # Progesterone receptor
            "ki67_index": np.random.uniform(0, 100),  # Proliferation marker
            "tumor_mutation_burden": np.random.uniform(0, 20),
            "homologous_recombination_deficiency": np.random.random(),
            "oncotype_dx_score": np.random.uniform(0, 100)  # Recurrence score
        }
        
        # Add gene expression levels for key genes
        for gene in self.key_genes:
            features[f"{gene}_expression"] = gene_expressions.get(gene, np.random.uniform(0, 10))
        
        logger.debug(f"Extracted genetic features: {list(features.keys())}")
        return features
    
    def _classify_genetic_risk(self, features: Dict[str, Any]) -> tuple:
        """
        Classify genetic risk based on extracted features.
        
        Returns:
            Tuple of (prediction, confidence)
        """
        risk_score = 0.0
        
        # High-risk mutations
        if features["brca1_mutation"] or features["brca2_mutation"]:
            risk_score += 0.4
        if features["tp53_mutation"]:
            risk_score += 0.2
        
        # Receptor status and markers
        if features["her2_amplification"] > 0.7:
            risk_score += 0.15
        if features["ki67_index"] > 30:  # High proliferation
            risk_score += 0.1
        if features["oncotype_dx_score"] > 25:  # Higher recurrence risk
            risk_score += 0.15
        
        # Normalize risk score
        risk_score = min(risk_score, 1.0)
        
        # Determine prediction
        if risk_score > 0.5:
            prediction = 1  # High genetic risk / malignant
            confidence = min(risk_score + 0.1, 0.95)
        else:
            prediction = 0  # Low genetic risk / benign
            confidence = min(1 - risk_score + 0.1, 0.95)
        
        return prediction, confidence
    
    def _identify_risk_genes(self, features: Dict[str, Any]) -> list:
        """Identify genes contributing to risk."""
        risk_genes = []
        
        if features.get("brca1_mutation"):
            risk_genes.append("BRCA1")
        if features.get("brca2_mutation"):
            risk_genes.append("BRCA2")
        if features.get("tp53_mutation"):
            risk_genes.append("TP53")
        if features.get("her2_amplification", 0) > 0.7:
            risk_genes.append("HER2")
        
        return risk_genes
    
    def _generate_explanation(self, features: Dict[str, Any], prediction: int, confidence: float) -> str:
        """Generate human-readable explanation of genetic analysis."""
        
        diagnosis = "high genetic risk" if prediction == 1 else "low genetic risk"
        conf_level = self.get_confidence_level(confidence)
        
        risk_genes = self._identify_risk_genes(features)
        
        if risk_genes:
            genes_text = ", ".join(risk_genes)
            explanation = f"Genomic analysis indicates {diagnosis} with {conf_level} confidence. "
            explanation += f"Significant findings in: {genes_text}. "
        else:
            explanation = f"Genomic analysis indicates {diagnosis} with {conf_level} confidence. "
            explanation += "No high-risk genetic mutations detected. "
        
        # Add receptor status
        if features.get("er_positive"):
            explanation += "ER-positive. "
        if features.get("pr_positive"):
            explanation += "PR-positive. "
        
        # Add proliferation info
        ki67 = features.get("ki67_index", 0)
        if ki67 > 30:
            explanation += f"High proliferation rate (Ki-67: {ki67:.1f}%)."
        elif ki67 > 14:
            explanation += f"Moderate proliferation rate (Ki-67: {ki67:.1f}%)."
        
        return explanation
